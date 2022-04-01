package snowbober.Systems;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import snowbober.Components.*;
import snowbober.ECS.Component;
import snowbober.ECS.System;
import snowbober.ECS.World;

import java.util.ArrayList;

public class CollisionSystem implements System {

    private final SpriteBatch batch;

    public CollisionSystem(SpriteBatch batch) {
        this.batch = batch;
    }

    @Override
    public void update(long gameFrame, float delta, World world) {

        ArrayList<Component[]> components = world.getEntitiesWithComponents(new int[]{
                CmpId.POSITION.ordinal(),
                CmpId.COLLISION.ordinal(),
                CmpId.PLAYER_CONTROLLED.ordinal()
        });

        for (int entityA = 0; entityA < World.MAX_ENTITIES; entityA++) {

            Position posA = (Position) components.get(0)[entityA];
            Collision colA = (Collision) components.get(1)[entityA];
            if (posA == null || colA == null) continue;

            for (int entityB = entityA + 1; entityB < World.MAX_ENTITIES; entityB++) {

                Position posB = (Position) components.get(0)[entityB];
                Collision colB = (Collision) components.get(1)[entityB];
                if (posB == null || colB == null) continue;

                CollisionType type = intersects(posA, colA, posB, colB);
                if (type == CollisionType.NONE) {
                    continue;
                }

                world.addComponentToEntity(entityA, new CollisionResponse(entityB, type, colB.type));
                world.addComponentToEntity(entityB, new CollisionResponse(entityA, type, colA.type));
            }
        }
    }

    private CollisionType intersects(Position posA, Collision colA, Position posB, Collision colB) {

        colA.rectangle.x = posA.x;
        colA.rectangle.y = posA.y;

        colB.rectangle.x = posB.x;
        colB.rectangle.y = posB.y;

        if (touch(colA.rectangle, colB.rectangle)) {
//            java.lang.System.out.println("DOTKNAL MNIE!");
            return CollisionType.TOUCH;
        }
        if (colA.rectangle.overlaps(colB.rectangle)) {
//            java.lang.System.out.println("KOLIZYJAAAAAAAAAAAAAAAAAAAAAA");
//            java.lang.System.out.println(colA.type + " z " + colB.type);

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
