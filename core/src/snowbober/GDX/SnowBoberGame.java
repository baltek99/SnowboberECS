package snowbober.GDX;

import com.badlogic.gdx.Game;
import snowbober.GDX.Screens.GameScreen;

public class SnowBoberGame extends Game {

    private GameScreen gameScreen;

    @Override
    public void create() {
        gameScreen = new GameScreen();
        setScreen(gameScreen);
    }

    @Override
    public void render() {
        super.render();
    }

    @Override
    public void dispose() {
        gameScreen.dispose();
    }

    @Override
    public void resize(int width, int height) {
        gameScreen.resize(width, height);
    }
}
