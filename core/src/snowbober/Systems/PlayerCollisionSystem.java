package snowbober.Systems;

import com.badlogic.gdx.graphics.Texture;
import snowbober.Components.*;
import snowbober.ECS.Component;
import snowbober.ECS.World;
import snowbober.ECS.System;
import snowbober.Util.ConstVals;

import java.util.ArrayList;

public class PlayerCollisionSystem implements System {

    @Override
    public void update(long gameFrame, float delta, World world) {
        ArrayList<Component[]> components = world.getEntitiesWithComponents(new int[]{
                CmpId.PLAYER_CONTROLLED.ordinal(),
                CmpId.COLLISION_RESPONSE.ordinal(),
                CmpId.POSITION.ordinal(),
                CmpId.VISUAL.ordinal()
        });

        for (int entity = 0; entity < world.MAX_ENTITIES; entity++) {
            if (World.isEntityOk(entity, components) == false) continue;

            CollisionResponse cr = (CollisionResponse) components.get(1)[entity];
            Position pos = (Position) components.get(2)[entity];
            PlayerControlled pc = (PlayerControlled) components.get(0)[entity];
            Visual vis = (Visual) components.get(3)[entity];

            if (cr.obstacle == ObstacleType.BOX || (cr.obstacle == ObstacleType.RAIL && pc.playerState == PlayerState.IDLE)) {
                world.killEntity(entity);
            } else if (cr.obstacle == ObstacleType.RAIL && pc.playerState == PlayerState.JUMPING) {
                pos.y = ConstVals.SLIDING_ON_RAIL_Y;
                pc.playerState = PlayerState.SLIDING;
                Texture texture = new Texture("bober-rail.png");
                components.get(3)[entity] = new Visual(texture, ConstVals.BOBER_ON_RAIL_WIDTH, ConstVals.BOBER_ON_RAIL_HEIGHT);
                world.removeComponentFromEntity(entity, cr);
            }
        }
    }
}
