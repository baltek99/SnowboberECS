package snowbober.Systems;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import snowbober.Components.Position;
import snowbober.Components.Score;
import snowbober.Components.TextField;
import snowbober.ECS.Component;
import snowbober.ECS.System;
import snowbober.ECS.World;
import snowbober.Enums.CmpId;

import java.util.ArrayList;

public class ResultRenderSystem implements System {
    private final SpriteBatch batch;
    private final BitmapFont font;

    public ResultRenderSystem(SpriteBatch batch) {
        this.batch = batch;

        FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal("cour.ttf"));
        FreeTypeFontGenerator.FreeTypeFontParameter parameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
        parameter.size = 30;

        font = generator.generateFont(parameter);
        generator.dispose();

        font.setColor(Color.WHITE);
    }

    @Override
    public void update(long gameFrame, float delta, World world) throws InterruptedException {
        ArrayList<Component[]> components = world.getEntitiesWithComponents(new int[]{
                CmpId.POSITION.ordinal(),
                CmpId.SCORE.ordinal(),
                CmpId.TEXT_FIELD.ordinal()
        });

        for (int entity = 0; entity < world.MAX_ENTITIES; entity++) {
            if (!World.isEntityOk(entity, components)) continue;

            Position pos = (Position) components.get(0)[entity];
            Score score = (Score) components.get(1)[entity];
            TextField text = (TextField) components.get(2)[entity];

            if (score != null) {
                batch.begin();
                font.draw(batch, "Player: " + text.text + "    Score: " + score.score, pos.x, pos.y);
                batch.end();
            }
        }
    }
}
