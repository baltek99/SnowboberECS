package snowbober.Components;

import com.badlogic.gdx.math.Rectangle;
import snowbober.ECS.ComponentWithId;

public class Collision extends ComponentWithId {
    public int width, height;
    public int radius;
    int collisionMask; // eg. 0b001
    public ObstacleType type;
    public Rectangle rectangle;

    public Collision(int width, int height, int rad, ObstacleType type) {
        super(CmpId.COLLISION.ordinal());
        this.width = width;
        this.height = height;
        radius = rad;
        this.type = type;
        rectangle = new Rectangle();
        rectangle.height = height;
        rectangle.width = width;
    }
}
