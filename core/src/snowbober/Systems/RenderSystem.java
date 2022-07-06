package snowbober.Systems;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import snowbober.Components.Position;
import snowbober.Components.Visual;
import snowbober.ECS.Component;
import snowbober.ECS.System;
import snowbober.ECS.World;
import snowbober.Enums.CmpId;

import java.util.ArrayList;

public class RenderSystem implements System {
    private final SpriteBatch batch;

    public RenderSystem(SpriteBatch batch) {
        this.batch = batch;
    }

    @Override
    public void update(long gameFrame, float delta, World world) throws InterruptedException {
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        ArrayList<Component[]> entities = world.getEntitiesWithComponents(new int[]{
                CmpId.POSITION.ordinal(),
                CmpId.VISUAL.ordinal()});

        for (int entity = 0; entity < world.MAX_ENTITIES; entity++) {
            Position pos = (Position) entities.get(0)[entity];
            Visual vis = (Visual) entities.get(1)[entity];

            if (pos == null || vis == null) continue;

            batch.begin();
            if (vis.texture != null && vis.isVisible) {
                batch.draw(vis.texture, pos.x, pos.y, vis.imgWidth / 2f, vis.imgHeight / 2f,
                        vis.imgWidth, vis.imgHeight, 1, 1, vis.rotation);
            }
            batch.end();
        }
    }
}
