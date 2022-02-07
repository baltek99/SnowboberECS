package snowbober.ECS;

public interface System {
    void update(long gameFrame, float delta, World world) throws InterruptedException;
}
