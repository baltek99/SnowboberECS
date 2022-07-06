package snowbober.Systems;

import com.badlogic.gdx.graphics.Texture;
import snowbober.Components.Jump;
import snowbober.Components.PlayerControlled;
import snowbober.Components.Position;
import snowbober.Components.Visual;
import snowbober.ECS.Component;
import snowbober.ECS.System;
import snowbober.ECS.World;
import snowbober.Enums.CmpId;
import snowbober.Enums.PlayerState;
import snowbober.Util.ConstValues;
import snowbober.Util.Util;

import java.util.ArrayList;

public class JumpSystem implements System {
    private int jumpHeight = 120;
    private float duration = 110;
    private float rotationSpeed = 3.4f;
    private float ollieUpSpeed = 1.2f;
    private float ollieDownSpeed = -0.4f;
    private int speedCount = 5;
    private int frame = 0;

    @Override
    public void update(long gameFrame, float delta, World world) {
        ArrayList<Component[]> components = world.getEntitiesWithComponents(new int[]{
                CmpId.POSITION.ordinal(),
                CmpId.PLAYER_CONTROLLED.ordinal(),
                CmpId.JUMP.ordinal(),
                CmpId.VISUAL.ordinal()
        });
        frame++;

        if (frame % ConstValues.NUMBER_OF_FRAMES_TO_INCREMENT == 0) {
            duration = duration - duration / speedCount;
            rotationSpeed = rotationSpeed + rotationSpeed / speedCount;
            ollieUpSpeed = ollieUpSpeed + ollieUpSpeed / speedCount;
            ollieDownSpeed = ollieDownSpeed + ollieDownSpeed / speedCount;
            speedCount++;
        }

        for (int entity = 0; entity < world.MAX_ENTITIES; entity++) {
            if (!World.isEntityOk(entity, components)) continue;

            Position pos = (Position) components.get(0)[entity];
            PlayerControlled pctrl = (PlayerControlled) components.get(1)[entity];
            Jump jump = ((Jump) components.get(2)[entity]);
            Visual vis = (Visual) components.get(3)[entity];

            if (pctrl.playerState == PlayerState.JUMPING || pctrl.playerState == PlayerState.JUMPING_ON_RAIL
                    || pctrl.playerState == PlayerState.JUMPING_FROM_CROUCH) {
                if (gameFrame >= jump.startJumpFrame + duration) {
                    pctrl.playerState = PlayerState.IDLE;
                    pos.y = ConstValues.IDLE_RIDE_Y;
                    vis.rotation = 0;
                    Texture texture = new Texture("bober-stand.png");
                    components.get(3)[entity] = new Visual(texture, ConstValues.BOBER_DEFAULT_WIDTH, ConstValues.BOBER_DEFAULT_HEIGHT);
                } else {
                    pos.y = (int) Util.lerp(
                            jump.jumpFrom,
                            jump.jumpFrom + jumpHeight,
                            Util.spike((gameFrame - jump.startJumpFrame) / duration)
                    );

                    if (pctrl.playerState == PlayerState.JUMPING_ON_RAIL) {
                        vis.rotation -= rotationSpeed;
                    } else if (pctrl.playerState == PlayerState.JUMPING_FROM_CROUCH) {
                        vis.rotation += rotationSpeed;
                    } else {
                        if ((gameFrame - jump.startJumpFrame) / duration < 0.15f)
                            vis.rotation += ollieUpSpeed;

                        else if (vis.rotation > -10)
                            vis.rotation += ollieDownSpeed;
                    }
                }
            }
        }
    }
}
