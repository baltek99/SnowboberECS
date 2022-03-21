package snowbober.Systems;

import com.badlogic.gdx.graphics.Texture;
import snowbober.Components.*;
import snowbober.ECS.World;
import snowbober.ECS.System;
import snowbober.Util.ConstValues;

public class ObstacleGeneratorSystem implements System {
    public int min, max, current, spawnRate;
    public Texture texBox;
    public Texture texRail;
    public Texture texGrid;
    public int width, height;
    public int speed;

    public ObstacleGeneratorSystem(int minIndex, int maxIndex, int width, int height) {
        this.min = minIndex;
        this.max = maxIndex;
        this.width = width;
        this.height = height;
        current = minIndex;
        spawnRate = 300;
        speed = -3;
        texBox = new Texture("box.png");
        texRail = new Texture("rail.png");
        texGrid = new Texture("rail.png");
    }

    @Override
    public void update(long gameFrame, float delta, World world) throws InterruptedException {
        if (gameFrame % spawnRate == 0) {
            int obstacle = current;
            current++;
            if (current > max) current = min;

            if (obstacle % 3 == 0) {
                createBox(world, obstacle);
                createScorePoint(world, obstacle + 5, width + 270);
            }
            else if (obstacle % 4 == 0) {
                createGrid(world, obstacle);
                createScorePoint(world, obstacle + 5, width + 500);
            }
            else {
                createRail(world, obstacle);
                createScorePoint(world, obstacle + 5, width + 500);
            }
        }
    }

    private void createGrid(World world, int  obstacleIndex) {
        world.addComponentToEntity(obstacleIndex, new Position(width, 110));
        world.addComponentToEntity(obstacleIndex, new Visual(texGrid, 200, 350));
        world.addComponentToEntity(obstacleIndex, new Move(speed));
        world.addComponentToEntity(obstacleIndex, new Collision(168,350, ObstacleType.GRID));
    }

    private void createRail(World world, int  obstacleIndex) {
        world.addComponentToEntity(obstacleIndex, new Position(width, 110));
        world.addComponentToEntity(obstacleIndex, new Visual(texRail, 300, 60));
        world.addComponentToEntity(obstacleIndex, new Move(speed));
        world.addComponentToEntity(obstacleIndex, new Collision(260,60, ObstacleType.RAIL));
    }

    private void createBox(World world, int obstacleIndex) {
        world.addComponentToEntity(obstacleIndex, new Position(width, 100));
        world.addComponentToEntity(obstacleIndex, new Visual(texBox, 70, 70));
        world.addComponentToEntity(obstacleIndex, new Move(speed));
        world.addComponentToEntity(obstacleIndex, new Collision(70,70, ObstacleType.BOX));
    }

    private void createScorePoint(World world, int scorePoint, int positionX) {
        world.addComponentToEntity(scorePoint, new Position(positionX, 0));
        world.addComponentToEntity(scorePoint, new Move(speed));
        world.addComponentToEntity(scorePoint, new Collision(1, ConstValues.V_HEIGHT, ObstacleType.SCORE_POINT));
//        world.addComponentToEntity(scorePoint, new Visual(texBox, 1, ConstVals.V_HEIGHT));
    }
}
