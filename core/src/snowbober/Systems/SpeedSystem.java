package snowbober.Systems;

import snowbober.Components.CmpId;
import snowbober.Components.Move;
import snowbober.Components.Position;
import snowbober.ECS.Component;
import snowbober.ECS.System;
import snowbober.ECS.World;
import snowbober.Util.ConstVals;

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

        if (gameFrame % ConstVals.NUMBER_OF_FRAMES_TO_INCREMENT == 0) {
            obstacleSpeed--;
        }

        for (int entity = 0; entity < World.MAX_ENTITIES; entity++) {
            if (!World.isEntityOk(entity, obstacleComponents)) {
                if (!World.isEntityOk(entity, backgroundComponents)) continue;
                if (gameFrame % ConstVals.NUMBER_OF_FRAMES_TO_INCREMENT == 0) {
                    Move mov = (Move) backgroundComponents.get(0)[entity];
                    mov.speed--;
                }
//                java.lang.System.out.println("Background " + entity + " move " + ((Move)backgroundComponents.get(0)[entity]).speed);
                // background
            } else {
                // obstacle
                Move mov = (Move) obstacleComponents.get(0)[entity];

                mov.speed = obstacleSpeed;

//                java.lang.System.out.println("Przeszkoda " + entity +" move " + ((Move)backgroundComponents.get(0)[entity]).speed);

            }
        }
    }
}
