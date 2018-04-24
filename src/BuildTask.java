public class BuildTask {
    //ATTRIBUTES
    private final Constructable constructable;
    private int ticksLeft;

    //CONSTRUCTOR
    public BuildTask(Constructable c) {
        this.constructable = c;
        this.ticksLeft = c.getBuildTime();
    }
    //decrement clock
    public int tick() {
        return ticksLeft--;
    }

    //GETTER
    public Constructable getConstructable() {
        return constructable;
    }
}
