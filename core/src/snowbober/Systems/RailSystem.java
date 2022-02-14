package snowbober.Systems;

import com.badlogic.gdx.graphics.Texture;
import snowbober.Components.*;
import snowbober.ECS.Component;
import snowbober.ECS.World;
import snowbober.ECS.System;

import java.util.ArrayList;

public class RailSystem implements System {
    @Override
    public void update(long gameFrame, float delta, World world) throws InterruptedException {
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
            if (pc.playerState == PlayerState.SLIDING && ((Position) components.get(2)[cr.collidingEntityId]).x <= -160) {
                world.removeComponentFromEntity(entity, cr);
                pc.playerState = PlayerState.IDLE;
                pos.y = 70;
                Texture texture = new Texture("bober-stand.png");
                components.get(3)[entity] = new Visual(texture, 180, 215);
//                Game.score++;
            }

        }
    }
}
