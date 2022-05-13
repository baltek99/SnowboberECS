package snowbober.Systems;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Texture;
import snowbober.Components.*;
import snowbober.ECS.Component;
import snowbober.ECS.System;
import snowbober.ECS.World;
import snowbober.Enums.CmpId;
import snowbober.Enums.PlayerState;
import snowbober.Util.ConstValues;

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
            if (!World.isEntityOk(entity, components)) continue;

            Position pos = (Position) components.get(0)[entity];
            PlayerControlled pctrl = (PlayerControlled) components.get(1)[entity];
            Jump jump = ((Jump) components.get(2)[entity]);
            Visual vis = (Visual) components.get(3)[entity];

            if (Gdx.input.isKeyPressed(Input.Keys.RIGHT) && pctrl.playerState == PlayerState.IDLE) {
                pos.x = pos.x + 5;
                if (pos.x > ConstValues.BOBER_MAX_X) pos.x = ConstValues.BOBER_MAX_X;
            } else if (Gdx.input.isKeyPressed(Input.Keys.LEFT) && pctrl.playerState == PlayerState.IDLE) {
                pos.x = pos.x - 5;
                if (pos.x < ConstValues.BOBER_MIN_X) pos.x = ConstValues.BOBER_MIN_X;
            }
            if (Gdx.input.isKeyJustPressed(Input.Keys.SPACE)) {
                if (pctrl.playerState == PlayerState.SLIDING) {
                    pctrl.playerState = PlayerState.JUMPING_ON_RAIL;
                    jump.jumpFrom = ConstValues.JUMP_FROM_RAIL_Y;
                    jump.startJumpFrame = gameFrame;
                    Texture texture = new Texture("bober-flip.png");
                    components.get(3)[entity] = new Visual(texture, ConstValues.BOBER_IN_JUMP_WIDTH, ConstValues.BOBER_IN_JUMP_HEIGHT);
                } else if (pctrl.playerState == PlayerState.IDLE || pctrl.playerState == PlayerState.CROUCH) {
                    Texture texture;
                    if (pctrl.playerState == PlayerState.CROUCH) {
                        pctrl.playerState = PlayerState.JUMPING_FROM_CROUCH;
                        texture = new Texture("bober-flip.png");
                    } else {
                        pctrl.playerState = PlayerState.JUMPING;
                        texture = new Texture("bober-jump.png");
                    }
                    jump.jumpFrom = ConstValues.JUMP_FROM_GROUND_Y;
                    jump.startJumpFrame = gameFrame;

                    components.get(3)[entity] = new Visual(texture, ConstValues.BOBER_IN_JUMP_WIDTH, ConstValues.BOBER_IN_JUMP_HEIGHT);
                }
            } else if (Gdx.input.isKeyJustPressed(Input.Keys.CONTROL_LEFT)) {
                if (pctrl.playerState == PlayerState.IDLE) {
                    pctrl.playerState = PlayerState.CROUCH;

                    pos.y = ConstValues.BOBER_CROUCH_POSITION_Y;
                    Texture texture = new Texture("bober-luzny.png");
                    components.get(3)[entity] = new Visual(texture, ConstValues.BOBER_CROUCH_WIDTH, ConstValues.BOBER_CROUCH_HEIGHT);
                } else if (pctrl.playerState == PlayerState.CROUCH) {
                    pctrl.playerState = PlayerState.IDLE;

                    pos.y = ConstValues.IDLE_RIDE_Y;
                    Texture texture = new Texture("bober-stand.png");
                    components.get(3)[entity] = new Visual(texture, ConstValues.BOBER_DEFAULT_WIDTH, ConstValues.BOBER_DEFAULT_HEIGHT);
                }
            }

        }
    }
}
