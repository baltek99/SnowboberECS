package snowbober.Systems;

import com.badlogic.gdx.graphics.Texture;
import snowbober.Components.*;
import snowbober.ECS.Component;
import snowbober.ECS.World;
import snowbober.ECS.System;

import java.util.ArrayList;
import java.util.Queue;

public class PlayerControlledSystem implements System {
    Queue<InputActions> actionsQueue;

    public PlayerControlledSystem(Queue<InputActions> queue) {
        actionsQueue = queue;
    }

    @Override
    public void update(long gameFrame, World world) {
        InputActions action = actionsQueue.poll();

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

            if (action == InputActions.JUMP && pctrl.playerState != PlayerState.JUMPING) {
                pctrl.playerState = PlayerState.JUMPING;
                jump.startJumpFrame = gameFrame;
                jump.jumpFrom = pos.y;
                vis.texture = new Texture("bober-jump.png");
            }

        }

    }

}
