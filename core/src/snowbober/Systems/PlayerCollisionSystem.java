package snowbober.Systems;

import com.badlogic.gdx.graphics.Texture;
import snowbober.Components.*;
import snowbober.ECS.Component;
import snowbober.ECS.System;
import snowbober.ECS.World;
import snowbober.Util.ConstValues;

import java.util.ArrayList;

public class PlayerCollisionSystem implements System {

    //todo problem do rozwiązania
//
//    przeszkody poruszają się z daną prędkością (domyślnie 3), bober stoi w danym miejscu
//    jeśli szerokość obiektu kolizji nie dzieli się przez 3 to bober nigdy nie będzie miał kolizji
//    typu TOUCH pod koniec pokonywania przeszkody.
//    Jeśli przeszkody będą przyśpieszać (a będą) to może być problem z określeniem typu kolizji
//

    @Override
    public void update(long gameFrame, float delta, World world) {
        ArrayList<Component[]> components = world.getEntitiesWithComponents(new int[]{
                CmpId.PLAYER_CONTROLLED.ordinal(),
                CmpId.COLLISION_RESPONSE.ordinal(),
                CmpId.POSITION.ordinal(),
                CmpId.VISUAL.ordinal(),
                CmpId.SCORE.ordinal(),
                CmpId.LIVES.ordinal()
        });

        for (int entity = 0; entity < world.MAX_ENTITIES; entity++) {
            if (World.isEntityOk(entity, components) == false) continue;

            PlayerControlled pc = (PlayerControlled) components.get(0)[entity];
            CollisionResponse cr = (CollisionResponse) components.get(1)[entity];
            Position pos = (Position) components.get(2)[entity];
            Visual vis = (Visual) components.get(3)[entity];
            Score score = (Score) components.get(4)[entity];
            Lives liv = (Lives) components.get(5)[entity];

            if (cr.obstacle == ObstacleType.SCORE_POINT) {
                score.score++;
                world.killEntity(cr.collidingEntityId);
                world.removeComponentFromEntity(entity, cr);
            } else if (cr.obstacle == ObstacleType.BOX || (cr.obstacle == ObstacleType.RAIL && pc.playerState == PlayerState.IDLE)) {
                removeLifeOrKill(world, entity, liv);
            } else if (cr.obstacle == ObstacleType.RAIL && (pc.playerState == PlayerState.JUMPING || pc.playerState == PlayerState.JUMPING_FROM_CROUCH)) {
                pos.y = ConstValues.SLIDING_ON_RAIL_Y;
                pc.playerState = PlayerState.SLIDING;
                Texture texture = new Texture("bober-rail.png");
                components.get(3)[entity] = new Visual(texture, ConstValues.BOBER_ON_RAIL_WIDTH, ConstValues.BOBER_ON_RAIL_HEIGHT);
                world.removeComponentFromEntity(entity, cr);
            } else if (cr.obstacle == ObstacleType.GRID && pc.playerState != PlayerState.CROUCH) {
                if (cr.type == CollisionType.INTERSECT) {
                    removeLifeOrKill(world, entity, liv);
                } else {
                    world.removeComponentFromEntity(entity, cr);
                }
            }
        }
    }

    private void removeLifeOrKill(World world, int entity, Lives liv) {
        if (liv.livesIds.size() == 1) {
            world.killEntity(entity);
            world.killEntity(liv.livesIds.poll());
        } else {
            int lifeID = liv.livesIds.poll();
            world.killEntity(lifeID);
            world.removeComponentFromEntity(entity, CmpId.COLLISION.ordinal());
            world.removeComponentFromEntity(entity, CmpId.COLLISION_RESPONSE.ordinal());
        }
    }
}
