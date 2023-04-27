package app.ui.views.game.map;

import javax.swing.JPanel;

import app.common.Logger;

public class MapPanel extends JPanel {

    public MapPanel() {
        buildClass();
        buildView();
    }

    private void buildView() {
        this.setLayout(null);
        this.setVisible(true);
    }

    private void buildClass() {

    }

    public void drawTerriotry(TerritoryComponent trComponent) {
        trComponent.setBounds(trComponent.getTerritory().territoryPosition.x,
                trComponent.getTerritory().territoryPosition.y,
                trComponent.getPreferredSize().width,
                trComponent.getPreferredSize().height);
        this.add(trComponent);
        Logger.log("Territory added: " + trComponent.getTerritory().toString());
        this.refresh();
    }

    public void refresh() {
        this.revalidate();
        this.repaint();
    }
}