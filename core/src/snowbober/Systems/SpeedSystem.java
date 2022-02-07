package snowbober.Systems;

import snowbober.Components.CmpId;
import snowbober.Components.Move;
import snowbober.Components.Position;
import snowbober.ECS.Component;
import snowbober.ECS.System;
import snowbober.ECS.World;

import java.util.ArrayList;

public class SpeedSystem implements System {

    @Override
    public void update(long gameFrame, float delta, World world) throws InterruptedException {
        ArrayList<Component[]> components = world.getEntitiesWithComponents(new int[]{
                CmpId.MOVE.ordinal()
        });

        for (int entity = 0; entity < World.MAX_ENTITIES; entity++) {
            if (World.isEntityOk(entity, components) == false) continue;

            if (gameFrame % 500 == 0) {
                Move mov = (Move) components.get(0)[entity];
//                mov.speed--;
            }
        }
    }
}
