package snowbober.Systems;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Texture;
import snowbober.Components.*;
import snowbober.ECS.Component;
import snowbober.ECS.System;
import snowbober.ECS.World;
import snowbober.Util.ConstVals;

import java.util.ArrayList;

public class PlayerControlledSystem implements System {

    @Override
    public void update(long gameFrame, float delta, World world) {

        ArrayList<Component[]> components = world.getEntitiesWithComponents(new int[]{
                CmpId.POSITION.ordinal(),
                CmpId.PLAYER_CONTROLLED.ordinal(),
                CmpId.JUMP.ordinal(),
                CmpId.VISUAL.ordinal()
        });

        for (int entity = 0; entity < world.MAX_ENTITIES; entity++) {
            if (World.isEntityOk(entity, components) == false) continue;

            Position pos = (Position) components.get(0)[entity];
            PlayerControlled pctrl = (PlayerControlled) components.get(1)[entity];
            Jump jump = ((Jump) components.get(2)[entity]);
            Visual vis = (Visual) components.get(3)[entity];

            if (Gdx.input.isKeyPressed(Input.Keys.SPACE)) {
                if (pctrl.playerState == PlayerState.SLIDING) {
//                    java.lang.System.out.println("SKOK Z RAILA!");
                    pctrl.playerState = PlayerState.JUMPING_ON_RAIL;
                    jump.jumpFrom = ConstVals.JUMP_FROM_RAIL_Y;
                    jump.startJumpFrame = gameFrame;
                    Texture texture = new Texture("bober-jump.png");
                    components.get(3)[entity] = new Visual(texture, ConstVals.BOBER_IN_JUMP_WIDTH, ConstVals.BOBER_IN_JUMP_HEIGHT);
                } else if (pctrl.playerState != PlayerState.JUMPING && pctrl.playerState != PlayerState.JUMPING_ON_RAIL) {
//                    java.lang.System.out.println("SKOK ZWYKlY");
                    pctrl.playerState = PlayerState.JUMPING;
                    jump.jumpFrom = ConstVals.JUMP_FROM_GROUND_Y;
                    jump.startJumpFrame = gameFrame;
                    Texture texture = new Texture("bober-jump.png");
                    components.get(3)[entity] = new Visual(texture, ConstVals.BOBER_IN_JUMP_WIDTH, ConstVals.BOBER_IN_JUMP_HEIGHT);
                }
            }

        }
    }
}
