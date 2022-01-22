package snowbober.ECS;

public interface System {
    void update(long gameFrame, World world) throws InterruptedException;
}
