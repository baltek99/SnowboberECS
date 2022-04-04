package snowbober.Systems;

import snowbober.Components.*;
import snowbober.ECS.Component;
import snowbober.ECS.System;
import snowbober.ECS.World;
import snowbober.Enums.CmpId;
import snowbober.Enums.PlayerState;

import java.util.ArrayList;

public class JumpOnRailSystem  implements System {

    @Override
    public void update(long gameFrame, float delta, World world) {
        ArrayList<Component[]> components = world.getEntitiesWithComponents(new int[]{
                CmpId.POSITION.ordinal(),
                CmpId.PLAYER_CONTROLLED.ordinal(),
                CmpId.JUMP.ordinal(),
                CmpId.VISUAL.ordinal(),
                CmpId.COLLISION_RESPONSE.ordinal()
        });

        for (int entity = 0; entity < world.MAX_ENTITIES; entity++) {
            if (!World.isEntityOk(entity, components)) continue;

            Position pos = (Position) components.get(0)[entity];
            PlayerControlled pctrl = (PlayerControlled) components.get(1)[entity];
            Jump jump = ((Jump) components.get(2)[entity]);
            Visual vis = (Visual) components.get(3)[entity];
            CollisionResponse cr = (CollisionResponse) components.get(4)[entity];

            if (pctrl.playerState == PlayerState.JUMPING_ON_RAIL) {
//                world.removeComponentFromEntity(entity, cr);
//                Collision obstacleCol = (Collision) world.getComponent(cr.collidingEntityId, CmpId.COLLISION.ordinal());
//                obstacleCol.height = 0;
                world.removeComponentFromEntity(cr.collidingEntityId, CmpId.COLLISION.ordinal());
                world.removeComponentFromEntity(cr.collidingEntityId, CmpId.COLLISION_RESPONSE.ordinal());
                world.removeComponentFromEntity(entity, CmpId.COLLISION_RESPONSE.ordinal());
//                java.lang.System.out.println("Rail niekolizyjny");
            }
        }
    }
}