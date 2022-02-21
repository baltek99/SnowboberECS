package snowbober.Systems;

import com.badlogic.gdx.graphics.Texture;
import snowbober.Components.*;
import snowbober.ECS.Component;
import snowbober.ECS.System;
import snowbober.ECS.World;
import snowbober.Util.ConstVals;
import snowbober.Util.Util;

import java.util.ArrayList;

public class JumpSystem implements System {
    static final int jumpHeight = 120;
    static final float duration = 110;

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

            if (pctrl.playerState == PlayerState.JUMPING || pctrl.playerState == PlayerState.JUMPING_ON_RAIL) {
                if (gameFrame == jump.startJumpFrame + duration) {
                    pctrl.playerState = PlayerState.IDLE;
                    pos.y = ConstVals.IDLE_RIDE_Y;
                    vis.rotation = 0;
                    Texture texture = new Texture("bober-stand.png");
                    components.get(3)[entity] = new Visual(texture, ConstVals.BOBER_DEFAULT_WIDTH, ConstVals.BOBER_DEFAULT_HEIGHT);
                } else {
//                    if ((gameFrame - jump.startJumpFrame) / duration < 0.1)
//                        pos.y = ConstVals.JUMP_FROM_GROUND_Y;

                    pos.y = (int) Util.lerp(
                            jump.jumpFrom,
                            jump.jumpFrom + jumpHeight,
                            Util.spike((gameFrame - jump.startJumpFrame) / duration)
                    );

//                    java.lang.System.out.println((gameFrame - jump.startJumpFrame) / duration);
                    if (pctrl.playerState == PlayerState.JUMPING_ON_RAIL) {
                        vis.rotation += 3.4f;
                    } else {
                        if ((gameFrame - jump.startJumpFrame) / duration < 0.15f)
                            vis.rotation += 1.3f;

                        else if (vis.rotation > -10)
                            vis.rotation -= 0.5;
                    }
                }
            }
        }
    }
}
