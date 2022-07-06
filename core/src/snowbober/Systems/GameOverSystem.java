package snowbober.Systems;

import snowbober.ECS.Component;
import snowbober.ECS.System;
import snowbober.ECS.World;
import snowbober.Enums.CmpId;
import snowbober.GDX.Screens.GameScreen;

import java.util.ArrayList;

public class GameOverSystem implements System {
    private final GameScreen gameScreen;

    public GameOverSystem(GameScreen gameScreen) {
        this.gameScreen = gameScreen;
    }

    @Override
    public void update(long gameFrame, float delta, World world) throws InterruptedException {
        ArrayList<Component[]> components = world.getEntitiesWithComponents(new int[]{
                CmpId.PLAYER_CONTROLLED.ordinal()
        });

        boolean pcExist = false;
        for (int entity = 0; entity < world.MAX_ENTITIES; entity++) {
            if (!World.isEntityOk(entity, components)) continue;
            pcExist = true;
        }

        if (!pcExist) {
            gameScreen.gameOver = true;
        }
    }
}
