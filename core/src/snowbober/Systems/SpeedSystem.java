package snowbober.Systems;

import snowbober.Components.Move;
import snowbober.ECS.Component;
import snowbober.ECS.System;
import snowbober.ECS.World;
import snowbober.Enums.CmpId;
import snowbober.Util.ConstValues;

import java.util.ArrayList;

public class SpeedSystem implements System {
    private int obstacleSpeed = -3;

    @Override
    public void update(long gameFrame, float delta, World world) throws InterruptedException {
        ArrayList<Component[]> backgroundComponents = world.getEntitiesWithComponents(new int[]{
                CmpId.MOVE.ordinal()
        });

        ArrayList<Component[]> obstacleComponents = world.getEntitiesWithComponents(new int[]{
                CmpId.MOVE.ordinal(),
                CmpId.COLLISION.ordinal(),
        });

        if (gameFrame % ConstValues.NUMBER_OF_FRAMES_TO_INCREMENT == 0) {
            obstacleSpeed--;
        }

        for (int entity = 0; entity < world.MAX_ENTITIES; entity++) {
            if (!World.isEntityOk(entity, obstacleComponents)) {
                if (!World.isEntityOk(entity, backgroundComponents)) continue;
                if (gameFrame % ConstValues.NUMBER_OF_FRAMES_TO_INCREMENT == 0) {
                    Move mov = (Move) backgroundComponents.get(0)[entity];
                    mov.speed--;
                }
            } else {
                Move mov = (Move) obstacleComponents.get(0)[entity];
                mov.speed = obstacleSpeed;
            }
        }
    }
}
