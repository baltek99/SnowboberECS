package snowbober.Systems;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import snowbober.Components.*;
import snowbober.ECS.Component;
import snowbober.ECS.System;
import snowbober.ECS.World;

import java.util.ArrayList;

public class ScoreRenderSystem implements System {

    private final SpriteBatch batch;
    private final BitmapFont font;

    public ScoreRenderSystem(SpriteBatch batch) {
        this.batch = batch;

        FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal("cour.ttf"));
        FreeTypeFontGenerator.FreeTypeFontParameter parameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
        parameter.size = 30;

        font = generator.generateFont(parameter);
        generator.dispose();

        font.setColor(Color.BLACK);
    }

    @Override
    public void update(long gameFrame, float delta, World world) throws InterruptedException {
        ArrayList<Component[]> components = world.getEntitiesWithComponents(new int[]{
                CmpId.POSITION.ordinal(),
                CmpId.SCORE_BIND.ordinal()
        });

        for (int entity = 0; entity < World.MAX_ENTITIES; entity++) {
            if (!World.isEntityOk(entity, components)) continue;

            Position pos = (Position) components.get(0)[entity];
            ScoreBind sb = (ScoreBind) components.get(1)[entity];
            Score score = (Score) world.getComponent(sb.playerId, CmpId.SCORE.ordinal());

            if (score != null) {
                batch.begin();
                font.draw(batch, "Score: " + score.score, pos.x, pos.y);
                batch.end();
            }
        }
    }
}
