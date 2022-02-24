package snowbober.Systems;

import com.badlogic.gdx.graphics.Texture;
import snowbober.Components.*;
import snowbober.ECS.Component;
import snowbober.ECS.System;
import snowbober.ECS.World;
import snowbober.Util.ConstVals;

import java.util.ArrayList;

public class ImmortalSystem implements System {

    int immortalDuration = 150;
    Texture playerTexture = new Texture("bober-stand.png");
    Visual playerVisual =  new Visual(playerTexture, ConstVals.BOBER_DEFAULT_WIDTH, ConstVals.BOBER_DEFAULT_HEIGHT);

    @Override
    public void update(long gameFrame, float delta, World world) throws InterruptedException {

        ArrayList<Component[]> components = world.getEntitiesWithComponents(new int[]{
                CmpId.PLAYER_CONTROLLED.ordinal(),
                CmpId.VISUAL.ordinal(),
                CmpId.COLLISION.ordinal()
        });

        for (int entity = 0; entity < world.MAX_ENTITIES; entity++) {
            PlayerControlled pc = (PlayerControlled) components.get(0)[entity];
            if (pc == null) continue;;

            Visual vis = (Visual) components.get(1)[entity];
            Collision col = (Collision) components.get(2)[entity];

            if (col == null && immortalDuration > 0) {
                if (immortalDuration % 20 == 0) {
                    if (vis == null) {
                        world.addComponentToEntity(entity, playerVisual);
                    } else {
                        world.removeComponentFromEntity(entity, CmpId.VISUAL.ordinal());
                    }
                }
                immortalDuration--;
            } else {
                immortalDuration = 150;
                world.addComponentToEntity(entity, new Collision(ConstVals.BOBER_DEFAULT_WIDTH, ConstVals.BOBER_DEFAULT_HEIGHT, ObstacleType.PLAYER));
                if (vis == null) {
                    world.addComponentToEntity(entity, playerVisual);
                }
            }
        }
    }
}
