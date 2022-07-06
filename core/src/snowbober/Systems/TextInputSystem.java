package snowbober.Systems;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import snowbober.Components.TextField;
import snowbober.ECS.Component;
import snowbober.ECS.System;
import snowbober.ECS.World;
import snowbober.Enums.CmpId;
import snowbober.GDX.Screens.GameScreen;

import java.util.ArrayList;

public class TextInputSystem implements System, Input.TextInputListener {
    private final World world;
    private final GameScreen gameScreen;
    public String text;

    public TextInputSystem(World world, GameScreen gameScreen) {
        this.world = world;
        this.gameScreen = gameScreen;
    }

    @Override
    public void update(long gameFrame, float delta, World world) throws InterruptedException {
        if (Gdx.input.isKeyJustPressed(Input.Keys.ANY_KEY)) {
            Gdx.input.getTextInput(this, "Podaj swój nick", "", "Player");
        }
    }

    @Override
    public void input(String text) {
        if (!text.isBlank()) {
            this.text = text;
        } else {
            this.text = "Player";
        }

        ArrayList<Component[]> components = world.getEntitiesWithComponents(new int[]{
                CmpId.TEXT_FIELD.ordinal()
        });
        for (int entity = 0; entity < world.MAX_ENTITIES; entity++) {
            if (!World.isEntityOk(entity, components)) continue;

            TextField input = (TextField) components.get(0)[entity];
            input.text = text;
        }
        gameScreen.isNameGiven = true;
    }

    @Override
    public void canceled() {

    }
}
