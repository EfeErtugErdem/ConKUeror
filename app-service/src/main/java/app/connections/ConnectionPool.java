package app.connections;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;

import app.common.EnvService;
import app.common.Logger;

public class ConnectionPool implements Runnable {
    private static final ArrayList<Connection> dbConnections = new ArrayList<>();
    private static final int maxInstance = 10;

    private static final String dbConnectionUrl = String.format(
            "jdbc:postgresql://%s:%s/%s?user=%s&password=%s",
            EnvService.getEnv("POSTGRES_HOST", "localhost"),
            EnvService.getEnv("POSTGRES_PORT", "5432"),
            EnvService.getEnv("POSTGRES_DB", null),
            EnvService.getEnv("POSTGRES_USER", "postgres"),
            EnvService.getEnv("POSTGRES_PASSWORD", null));

    public ConnectionPool() {
        Thread thread = new Thread(this);
        thread.start();
    }

    private static Connection newConnection() throws NullPointerException {
        DatabaseConnection db = new DatabaseConnection(dbConnectionUrl);
        Connection connection = db.getConnection();
        if (connection != null) {
            return db.getConnection();
        }
        throw new NullPointerException("Connection is missing");
    }

    private static Connection addConnectionToPool(Connection connection) {
        if (dbConnections.size() >= maxInstance) {
            Logger.error("Database connection pool is full");
            return null;
        }
        dbConnections.add(connection);
        return connection;
    }

    private static Connection findValidConnection() {
        for (Connection connection : dbConnections) {
            try {
                if (connection.isValid(2)) {
                    return connection;
                }
            } catch (SQLException e) {
                Logger.error(e);
            }
        }
        return null;
    }

    public static Connection getValidConnection() {
        Connection connection = findValidConnection();
        if (connection != null) {
            return connection;
        }
        return addConnectionToPool(newConnection());
    }

    @Override
    public void run() {
        try {
            while (true) {
                for (int i = 0; i < dbConnections.size(); i++) {
                    Connection connection = dbConnections.get(i);
                    if (connection.isValid(5)) {
                        connection.close();
                        dbConnections.remove(connection);
                    }
                }
                Thread.sleep(5000);
                Logger.info(String.format("Active connections: %s", dbConnections.toString()));
            }
        } catch (InterruptedException | SQLException e) {
            e.printStackTrace();
        }
    }
}
