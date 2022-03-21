package snowbober.Systems;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import snowbober.Components.*;
import snowbober.ECS.Component;
import snowbober.ECS.System;
import snowbober.ECS.World;

import java.util.ArrayList;

public class StartSystem implements System {

    @Override
    public void update(long gameFrame, float delta, World world) throws InterruptedException {

        ArrayList<Component[]> components = world.getEntitiesWithComponents(new int[]{
                CmpId.POSITION.ordinal(),
                CmpId.VISUAL.ordinal()
        });

        for (int entity = 0; entity < world.MAX_ENTITIES; entity++) {
            if (World.isEntityOk(entity, components) == false) continue;

            Position pos = (Position) components.get(0)[entity];
            Visual vis = (Visual) components.get(3)[entity];

            if (Gdx.input.isKeyPressed(Input.Keys.SPACE)) {

            }

        }
    }
}
