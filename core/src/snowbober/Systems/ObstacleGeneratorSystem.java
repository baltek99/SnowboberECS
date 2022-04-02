package snowbober.Systems;

import com.badlogic.gdx.graphics.Texture;
import snowbober.Components.*;
import snowbober.ECS.System;
import snowbober.ECS.World;
import snowbober.Util.ConstValues;

import java.util.Random;

public class ObstacleGeneratorSystem implements System {
    public final int maxNumberOfObstacles;
    public int obstacleMin, obstacleMax;
    public int current;
    public int spawnRate;
    public Texture texBox;
    public Texture texRail;
    public Texture texFatPipe;
    public Texture texGrid;
    public Texture texGridStick;
    public int gridMin, gridMax;
    public int scoreMin, scoreMax;
    public int width, height;
    public int initialSpeed;
    public int speedCount;
    public int frame;
    private final Random random;

    public ObstacleGeneratorSystem(int obstaclesMinIndex, int gridMinIndex, int scoreMinIndex, int maxNumberOfObstacles) {
        this.maxNumberOfObstacles = maxNumberOfObstacles;
        this.obstacleMin = obstaclesMinIndex;
        this.obstacleMax = obstaclesMinIndex + maxNumberOfObstacles - 1;
        this.width = ConstValues.V_WIDTH;
        this.height = ConstValues.V_HEIGHT;
        this.gridMin = gridMinIndex;
        this.gridMax = gridMinIndex + maxNumberOfObstacles - 1;
        this.scoreMin = scoreMinIndex;
        this.scoreMax = scoreMinIndex + maxNumberOfObstacles - 1;
        random = new Random();
        current = 0;
        spawnRate = 300;
        initialSpeed = -3;
        speedCount = 3;
        texBox = new Texture("box.png");
        texRail = new Texture("rail.png");
        texFatPipe = new Texture("grubas.png");
        texGrid = new Texture("grid.png");
        texGridStick = new Texture("grid-stick.png");
    }

    @Override
    public void update(long gameFrame, float delta, World world) throws InterruptedException {
        frame++;

        if (frame % ConstValues.NUMBER_OF_FRAMES_TO_INCREMENT == 0) {
            spawnRate = spawnRate - spawnRate / speedCount;
            speedCount++;
            frame = 1;
//            java.lang.System.out.println("Spawn rate " + spawnRate + " speed " + speedCount);
        }

        if (frame % spawnRate == 0) {
//            java.lang.System.out.println("Przeszkoda spawn rate " + spawnRate + " gameFrame " + frame);
            current++;
            if (current >= maxNumberOfObstacles) current = 0;

            int x = random.nextInt(1000);

            if (x < 333) {
                createBox(world);
                createScorePoint(world, width + 270);
            } else if (x < 666) {
                createGridFlag(world);
                createGridStick(world);
                createScorePoint(world, width + 500);
            } else {
                createRail(world);
                createScorePoint(world, width + 500);
            }
        }
    }

    private void createGridFlag(World world) {
        createGrid(world, gridMin, texGrid);
        world.addComponentToEntity(gridMin + current, new Collision(ConstValues.GRID_WIDTH, ConstValues.GRID_HEIGHT, ObstacleType.GRID));
    }

    private void createGridStick(World world) {
        createGrid(world, obstacleMin, texGridStick);
        world.addComponentToEntity(obstacleMin + current, new Collision(0, 0, ObstacleType.GRID));
    }

    private void createGrid(World world, int obstacleIndex, Texture texGridStick) {
        world.addComponentToEntity(obstacleIndex + current, new Position(width, 60));
        world.addComponentToEntity(obstacleIndex + current, new Visual(texGridStick, ConstValues.GRID_WIDTH, ConstValues.GRID_HEIGHT));
        world.addComponentToEntity(obstacleIndex + current, new Move(initialSpeed));
    }

    private void createRail(World world) {
        Texture texture = random.nextInt(100) < 50 ? texRail : texFatPipe;
        world.addComponentToEntity(obstacleMin + current, new Position(width, 110));
        world.addComponentToEntity(obstacleMin + current, new Visual(texture, ConstValues.RAIL_WIDTH, ConstValues.RAIL_HEIGHT));
        world.addComponentToEntity(obstacleMin + current, new Move(initialSpeed));
        world.addComponentToEntity(obstacleMin + current, new Collision( ConstValues.RAIL_WIDTH - 50, ConstValues.RAIL_HEIGHT, ObstacleType.RAIL));
    }

    private void createBox(World world) {
        world.addComponentToEntity(obstacleMin + current, new Position(width, 100));
        world.addComponentToEntity(obstacleMin + current, new Visual(texBox, ConstValues.BOX_WIDTH, ConstValues.BOX_HEIGHT));
        world.addComponentToEntity(obstacleMin + current, new Move(initialSpeed));
        world.addComponentToEntity(obstacleMin + current, new Collision(ConstValues.BOX_WIDTH, ConstValues.BOX_HEIGHT, ObstacleType.BOX));
    }

    private void createScorePoint(World world, int positionX) {
        world.addComponentToEntity(scoreMin + current, new Position(positionX, 0));
        world.addComponentToEntity(scoreMin + current, new Move(initialSpeed));
        world.addComponentToEntity(scoreMin + current, new Collision(ConstValues.SCORE_WIDTH, ConstValues.SCORE_HEIGHT, ObstacleType.SCORE_POINT));
    }
}
