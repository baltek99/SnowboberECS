package snowbober.Systems;

import snowbober.Components.CmpId;
import snowbober.Components.Position;
import snowbober.ECS.Component;
import snowbober.ECS.World;
import snowbober.ECS.System;

import java.util.ArrayList;

public class BackgroundGeneratorSystem implements System {
    public int width, height;

    public BackgroundGeneratorSystem(int width, int height) {
        this.width = width;
        this.height = height;
    }

    @Override
    public void update(long gameFrame, float delta, World world) {

        ArrayList<Component[]> components = world.getEntitiesWithComponents(new int[]{
                CmpId.POSITION.ordinal(),
                CmpId.MOVE.ordinal()
        });

        for (int entity = 0; entity < 2; entity++) {
            if (World.isEntityOk(entity, components) == false) continue;

            Position pos = (Position) components.get(0)[entity];

            if (Math.abs(pos.x) >= width) {
                pos.x = width;
            }
        }
    }
}
