package snowbober.Systems;

import snowbober.Components.Position;
import snowbober.ECS.Component;
import snowbober.ECS.System;
import snowbober.ECS.World;
import snowbober.Enums.CmpId;

import java.util.ArrayList;

public class BackgroundGeneratorSystem implements System {
    public final int width, height;

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
            if (!World.isEntityOk(entity, components)) continue;

            Position pos = (Position) components.get(0)[entity];

            if (pos.x <= -width) {
                pos.x = width + pos.x % width;
            }
        }
    }
}
