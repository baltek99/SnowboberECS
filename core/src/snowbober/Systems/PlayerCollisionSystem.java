package snowbober.Systems;

import com.badlogic.gdx.graphics.Texture;
import snowbober.Components.*;
import snowbober.ECS.Component;
import snowbober.ECS.System;
import snowbober.ECS.World;
import snowbober.Util.ConstValues;

import java.util.ArrayList;

public class PlayerCollisionSystem implements System {

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
//                java.lang.System.out.println("Punkt!");
            } else if (cr.obstacle == ObstacleType.BOX || (cr.obstacle == ObstacleType.RAIL && pc.playerState == PlayerState.IDLE)) {
                removeLifeOrKill(world, entity, liv);
                pos.y = ConstValues.BOBER_DEFAULT_POSITION_Y;
//                pc.playerState = PlayerState.IMMORTAL;
            } else if (cr.obstacle == ObstacleType.RAIL && (pc.playerState == PlayerState.JUMPING ||
                    pc.playerState == PlayerState.JUMPING_FROM_CROUCH || pc.playerState == PlayerState.JUMPING_ON_RAIL)) {
//                java.lang.System.out.println("Sliding on rail");
                pos.y = ConstValues.SLIDING_ON_RAIL_Y;
                pc.playerState = PlayerState.SLIDING;
                Texture texture = new Texture("bober-rail.png");
                components.get(3)[entity] = new Visual(texture, ConstValues.BOBER_ON_RAIL_WIDTH, ConstValues.BOBER_ON_RAIL_HEIGHT);
                world.removeComponentFromEntity(entity, cr);
            } else if (cr.obstacle == ObstacleType.GRID) {
                if (pc.playerState != PlayerState.CROUCH) {
                    removeLifeOrKill(world, entity, liv);
                    pos.y = ConstValues.BOBER_DEFAULT_POSITION_Y;
//                    pc.playerState = PlayerState.IMMORTAL;
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
