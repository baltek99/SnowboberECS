package snowbober.Systems;

import com.badlogic.gdx.graphics.Texture;
import snowbober.Components.*;
import snowbober.ECS.Component;
import snowbober.ECS.System;
import snowbober.ECS.World;
import snowbober.Util.Game;
import snowbober.Util.Util;

import java.util.ArrayList;

public class JumpSystem implements System {
    static final int jumpHeight = -200;
    static final float duration = 100;

    @Override
    public void update(long gameFrame, World world) {
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

            if (pctrl.playerState == PlayerState.JUMPING) {
                if (gameFrame == jump.startJumpFrame + duration) {
                    pctrl.playerState = PlayerState.IDLE;
                    pos.y = jump.jumpFrom;
                    vis.texture = new Texture("bober-stand.png");
                    Game.score++;
                } else {
                    // frames
                    pos.y = (int) Util.lerp(
                            jump.jumpFrom,
                            jump.jumpFrom + jumpHeight,
                            Util.spike((gameFrame - jump.startJumpFrame) / duration)
                    );
                }
            }
        }
    }


}
