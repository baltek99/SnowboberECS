package snowbober.Systems;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import snowbober.Components.Collision;
import snowbober.Components.CollisionResponse;
import snowbober.Components.Position;
import snowbober.Components.Visual;
import snowbober.ECS.Component;
import snowbober.ECS.System;
import snowbober.ECS.World;
import snowbober.Enums.CmpId;
import snowbober.Enums.CollisionType;
import snowbober.Util.RotatedRectangle;

import java.util.ArrayList;

public class CollisionSystem implements System {
    @Override
    public void update(long gameFrame, float delta, World world) {
        ArrayList<Component[]> components = world.getEntitiesWithComponents(new int[]{
                CmpId.POSITION.ordinal(),
                CmpId.COLLISION.ordinal(),
                CmpId.VISUAL.ordinal()
        });

        for (int entityA = 0; entityA < world.MAX_ENTITIES; entityA++) {
            Position posA = (Position) components.get(0)[entityA];
            Collision colA = (Collision) components.get(1)[entityA];
            Visual visA = (Visual) components.get(2)[entityA];
            if (posA == null || colA == null || visA == null) continue;

            for (int entityB = entityA + 1; entityB < world.MAX_ENTITIES; entityB++) {
                Position posB = (Position) components.get(0)[entityB];
                Collision colB = (Collision) components.get(1)[entityB];
                Visual visB = (Visual) components.get(2)[entityB];
                if (posB == null || colB == null || visB == null) continue;

                CollisionType type = intersects(posA, colA, visA, posB, colB, visB);
                if (type == CollisionType.NONE) {
                    continue;
                }

                world.addComponentToEntity(entityA, new CollisionResponse(entityB, type, colB.type));
                world.addComponentToEntity(entityB, new CollisionResponse(entityA, type, colA.type));
            }
        }
    }

    private CollisionType intersects(Position posA, Collision colA, Visual visA, Position posB, Collision colB, Visual visB) {
        colA.rectangle.x = posA.x;
        colA.rectangle.y = posA.y;

        colB.rectangle.x = posB.x;
        colB.rectangle.y = posB.y;

        RotatedRectangle rectA = new RotatedRectangle(colA.rectangle, visA.rotation);
        RotatedRectangle rectB = new RotatedRectangle(colB.rectangle, visB.rotation);

        if (rectA.intersects(rectB)) {
            return CollisionType.INTERSECT;
        }

        return CollisionType.NONE;
    }

    private boolean touch(Rectangle s, Rectangle r) {
        boolean left = s.x == r.x + r.width && s.x + s.width > r.x && s.y < r.y + r.height && s.y + s.height > r.y;
        boolean right = s.x < r.x + r.width && s.x + s.width == r.x && s.y < r.y + r.height && s.y + s.height > r.y;
        boolean down = s.x < r.x + r.width && s.x + s.width > r.x && s.y == r.y + r.height && s.y + s.height > r.y;
        boolean up = s.x < r.x + r.width && s.x + s.width > r.x && s.y < r.y + r.height && s.y + s.height == r.y;

        return left || right || down || up;
    }

}
