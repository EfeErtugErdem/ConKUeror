package app.domain.models.army;

public class Chivalry extends ArmyUnit {
    protected Chivalry(String description) {
        super(Chivalry.class.getName(), description);
        this.value = 5;
    }
}
