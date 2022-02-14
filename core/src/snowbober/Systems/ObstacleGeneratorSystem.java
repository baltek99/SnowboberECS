package snowbober.Systems;

import com.badlogic.gdx.graphics.Texture;
import snowbober.Components.*;
import snowbober.ECS.World;
import snowbober.ECS.System;
import snowbober.Util.ConstVals;

public class ObstacleGeneratorSystem implements System {
    public int min, max, current, spawnRate;
    public Texture texBox;
    public Texture texRail;
    public int width, height;

    public ObstacleGeneratorSystem(int minIndex, int maxIndex, int width, int height) {
        this.min = minIndex;
        this.max = maxIndex;
        this.width = width;
        this.height = height;
        current = minIndex;
        spawnRate = 300;
        texBox = new Texture("box.png");
        texRail = new Texture("rail.png");
    }

    @Override
    public void update(long gameFrame, float delta, World world) throws InterruptedException {
        if (gameFrame % spawnRate == 0) {
//            java.lang.System.out.println(gameFrame);
            int obstacle = current;
//            current = ((current + 1) % (max - min)) + min;
            current++;
            if (current > max) current = min;

//            java.lang.System.out.println("NUMER " + obstacle);
            if (obstacle % 3 == 0) {
                createBox(world, obstacle);
                createScorePoint(world, obstacle + 5, width + 270);
            }
            else {
                createRail(world, obstacle);
                createScorePoint(world, obstacle + 5, width + 500);
            }
        }
    }

    public void createRail(World world, int rail) {
        world.addComponentToEntity(rail, new Position(width, 110));
        world.addComponentToEntity(rail, new Visual(texRail, 300, 60));
        world.addComponentToEntity(rail, new Move(-3));
        world.addComponentToEntity(rail, new Collision(260,60, ObstacleType.RAIL));
    }

    public void createBox(World world, int box) {
        world.addComponentToEntity(box, new Position(width, 100));
        world.addComponentToEntity(box, new Visual(texBox, 70, 70));
        world.addComponentToEntity(box, new Move(-3));
        world.addComponentToEntity(box, new Collision(70,70, ObstacleType.BOX));
    }

    public void createScorePoint(World world, int scorePoint, int positionX) {
        world.addComponentToEntity(scorePoint, new Position(positionX, 0));
        world.addComponentToEntity(scorePoint, new Move(-3));
        world.addComponentToEntity(scorePoint, new Collision(1, ConstVals.V_HEIGHT, ObstacleType.SCORE_POINT));
        world.addComponentToEntity(scorePoint, new Visual(texBox, 1, ConstVals.V_HEIGHT));
    }
}
