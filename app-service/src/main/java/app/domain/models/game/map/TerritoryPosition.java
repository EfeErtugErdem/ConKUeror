package app.domain.models.game.map;

public class TerritoryPosition {
    private int x;
    private int y;

    public TerritoryPosition(int xPos, int yPos) {
        this.x = xPos;
        this.y = yPos;
    }

    public TerritoryPosition() {
    }

    @Override
    public String toString() {
        String info = "";
        info += "X: " + x;
        info += "\t";
        info += "Y:" + y;
        return info;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

}
