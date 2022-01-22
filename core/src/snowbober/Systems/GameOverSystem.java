package snowbober.Systems;

import snowbober.ECS.System;
import snowbober.Components.CmpId;
import snowbober.ECS.Component;
import snowbober.ECS.World;
import snowbober.GDX.Screens.GameScreen;
import snowbober.Util.Game;

import java.util.ArrayList;

public class GameOverSystem implements System {
    boolean pcExist;
    GameScreen gameScreen;

    public GameOverSystem(GameScreen gameScreen) {
        this.gameScreen = gameScreen;
    }

    @Override
    public void update(long gameFrame, World world) throws InterruptedException {
        ArrayList<Component[]> components = world.getEntitiesWithComponents(new int[]{
                CmpId.PLAYER_CONTROLLED.ordinal()
        });

        pcExist = false;
        for (int entity = 0; entity < world.MAX_ENTITIES; entity++) {
            if (World.isEntityOk(entity, components) == false) continue;

            pcExist = true;
            //java.lang.System.out.println("true");
        }

        if (!pcExist) {
            java.lang.System.out.println("GAME OVER");
            Game.gameOver = true;
            Game.initGameOver = true;
            gameScreen.gameOver = true;
        }
    }
}
