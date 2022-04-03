package snowbober.Components;

import com.badlogic.gdx.math.Rectangle;
import snowbober.ECS.ComponentWithId;
import snowbober.Enums.CmpId;
import snowbober.Enums.ObstacleType;

public class Collision extends ComponentWithId {
    public int width, height;
    public ObstacleType type;
    public Rectangle rectangle;

    public Collision(int width, int height, ObstacleType type) {
        super(CmpId.COLLISION.ordinal());
        this.width = width;
        this.height = height;
        this.type = type;
        rectangle = new Rectangle();
        rectangle.height = height;
        rectangle.width = width;
    }
}
