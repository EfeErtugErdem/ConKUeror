package app.ui.controllers.game.map;

import app.domain.models.game.GameState;
import app.domain.models.game.map.Territory;
import app.domain.services.GameManagerService;
import app.domain.services.base.BasePublisher;
import app.domain.services.base.ISubscriber;
import app.ui.controllers.game.state.BaseStatePanelController;
import app.ui.views.game.map.TerritoryComponent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;

public class TerritoryComponentController extends BasePublisher<Territory> implements ISubscriber<GameState> {
    private TerritoryComponent territoryComponent;
    private Territory territory;
    private GameState _currentState;

    public TerritoryComponentController(Territory territory) throws IOException {
        super(territory);
        this.territory = territory;
        this.territoryComponent = new TerritoryComponent(territory.getImage());
        updateComponentBounds();
        setupListeners();
        GameManagerService.getInstance().addSubscriber(this);
    }

    private void setupListeners() {
        territoryComponent.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                handleTerritoryClicked();
            }

            @Override
            public void mouseEntered(MouseEvent e) {
                handleTerritoryEnter();
            }
        });
    }

    public TerritoryComponent getTerritoryComponent() {
        return territoryComponent;
    }

    public void updateComponentBounds() {
        territoryComponent.setBounds(territory.getTerritoryPosition().getX(),
                territory.getTerritoryPosition().getY(),
                territoryComponent.getPreferredSize().width,
                territoryComponent.getPreferredSize().height);
    }

    private void handleTerritoryClicked() {
        if (_currentState == GameState.BUILDING_STATE) {
            territoryComponent.toggleVisiblity();
            territoryComponent.repaint();
            territory.setIsOpen(territoryComponent.getVisibilty());
        } else {
            super.notifySubscribersOfType(BaseStatePanelController.class);
        }
    }

    private void handleTerritoryEnter() {
        super.notifyCustomSubscribers(MapPanelController.class);
    }

    @Override
    public void update(GameState message) {
        _currentState = message;
    }
}
