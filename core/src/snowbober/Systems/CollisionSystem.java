package snowbober.Systems;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Rectangle;
import snowbober.Components.*;
import snowbober.ECS.Component;
import snowbober.ECS.World;
import snowbober.ECS.System;

import java.util.ArrayList;

public class CollisionSystem implements System {

    SpriteBatch batch;

    public CollisionSystem(SpriteBatch batch) {
        this.batch = batch;
    }

    @Override
    public void update(long gameFrame, World world) {

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

    CollisionType intersects(Position posA, Collision colA, Position posB, Collision colB) {

        colA.rectangle.x = posA.x;
        colA.rectangle.y = posA.y;

        colB.rectangle.x = posB.x;
        colB.rectangle.y = posB.y;

        if (touch(colA.rectangle, colB.rectangle)) {
            java.lang.System.out.println("DOTKNAL MNIE!");
            return CollisionType.TOUCH;
        }
        if (colA.rectangle.overlaps(colB.rectangle)) {
//            java.lang.System.out.println("KOLIZYJAAAAAAAAAAAAAAAAAAAAAA");
            return CollisionType.INTERSECT;
        }

//        int distance = (int) Math.sqrt(((posA.x - posB.x) * (posA.x - posB.x)) + ((posA.y - posB.y) * (posA.y - posB.y)));
//        java.lang.System.out.println("Dist: " + distance + " Pos: " + posA.x  + " " + posB.x);
//        if (distance == colA.radius + colB.radius) {
//            return CollisionType.TOUCH;
//        } else if (distance < colA.radius + colB.radius) {
//
//            return CollisionType.INTERSECT;
//        }

        return CollisionType.NONE;
    }

    public boolean touch (Rectangle s, Rectangle r) {
        boolean lewym =  s.x == r.x + r.width && s.x + s.width > r.x && s.y < r.y + r.height && s.y + s.height > r.y;
        boolean prawym =  s.x < r.x + r.width && s.x + s.width == r.x && s.y < r.y + r.height && s.y + s.height > r.y;
        boolean dolem =  s.x < r.x + r.width && s.x + s.width > r.x && s.y == r.y + r.height && s.y + s.height > r.y;
        boolean gora =  s.x < r.x + r.width && s.x + s.width > r.x && s.y < r.y + r.height && s.y + s.height == r.y;

        return lewym || prawym || dolem || gora;
    }

}
