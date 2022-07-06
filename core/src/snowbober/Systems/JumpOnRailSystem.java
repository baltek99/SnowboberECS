package snowbober.Systems;

import snowbober.Components.CollisionResponse;
import snowbober.Components.PlayerControlled;
import snowbober.ECS.Component;
import snowbober.ECS.System;
import snowbober.ECS.World;
import snowbober.Enums.CmpId;
import snowbober.Enums.PlayerState;

import java.util.ArrayList;

public class JumpOnRailSystem implements System {

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

            PlayerControlled pctrl = (PlayerControlled) components.get(1)[entity];
            CollisionResponse cr = (CollisionResponse) components.get(4)[entity];

            if (pctrl.playerState == PlayerState.JUMPING_ON_RAIL) {
                world.removeComponentFromEntity(cr.collidingEntityId, CmpId.COLLISION.ordinal());
                world.removeComponentFromEntity(cr.collidingEntityId, CmpId.COLLISION_RESPONSE.ordinal());
                world.removeComponentFromEntity(entity, CmpId.COLLISION_RESPONSE.ordinal());
            }
        }
    }
}