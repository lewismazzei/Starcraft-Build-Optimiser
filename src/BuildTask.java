import java.util.Optional;

public class BuildTask {
    private final Constructable constructable;
    private int ticksLeft;

    public BuildTask(Constructable c) {
        this.constructable = c;
        this.ticksLeft = c.getBuildTime();
    }

    public int tick() {
        return ticksLeft--;
    }

    public Constructable getConstructable() {
        return constructable;
    }
}
