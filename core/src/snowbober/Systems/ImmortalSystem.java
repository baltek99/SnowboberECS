package snowbober.Systems;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import snowbober.Components.*;
import snowbober.ECS.Component;
import snowbober.ECS.System;
import snowbober.ECS.World;
import snowbober.Util.ConstValues;

import java.util.ArrayList;

public class ImmortalSystem implements System {
    int initialImmortalDurationVal = 150;
    int immortalDuration = 150;
    TextureRegion playerTexture = new TextureRegion(new Texture("bober-stand.png"));

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

            if (gameFrame == ConstValues.NUMBER_OF_FRAMES_TO_INCREMENT) {
                if (immortalDuration == initialImmortalDurationVal) {
                    immortalDuration = 100;
                }
                initialImmortalDurationVal = 100;
            }

            if (col == null && immortalDuration > 0) {
                if (immortalDuration % 20 == 0) {
                    if (vis.texture == null) {
                        vis.texture = new TextureRegion(playerTexture);
                    } else {
                        playerTexture = vis.texture;
                        vis.texture = null;
                    }
                }
                immortalDuration--;
            } else if (col == null) {
                immortalDuration = initialImmortalDurationVal;
                world.addComponentToEntity(entity, new Collision(ConstValues.BOBER_DEFAULT_WIDTH, ConstValues.BOBER_DEFAULT_HEIGHT, ObstacleType.PLAYER));
                if (vis.texture == null) {
                    vis.texture = new TextureRegion(playerTexture);
                }
            }
        }
    }
}
