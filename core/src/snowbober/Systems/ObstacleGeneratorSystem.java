package snowbober.Systems;

import com.badlogic.gdx.graphics.Texture;
import snowbober.Components.*;
import snowbober.ECS.World;
import snowbober.ECS.System;

public class ObstacleGeneratorSystem implements System {
    public int min, max, current, spawnRate;
    public Texture texBox;
    public Texture texRail;
    public int width, height;

    public ObstacleGeneratorSystem(int min, int max, int width, int height) {
        this.min = min;
        this.max = max;
        this.width = width;
        this.height = height;
        current = min;
        spawnRate = 300;
        texBox = new Texture("box.png");
        texRail = new Texture("rail.png");
    }

    @Override
    public void update(long gameFrame, float delta, World world) throws InterruptedException {
        if (gameFrame % spawnRate == 0) {
            java.lang.System.out.println(gameFrame);
            int obstacle = current;
            current = ((current + 1) % (max - min)) + min;

            if (obstacle % 3 == 0) createBox(world, obstacle);
            else createRail(world, obstacle);
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
}
