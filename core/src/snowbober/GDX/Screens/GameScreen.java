package snowbober.GDX.Screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import snowbober.Components.CmpId;
import snowbober.Components.Position;
import snowbober.Components.Visual;
import snowbober.ECS.Component;
import snowbober.ECS.World;
import snowbober.GDX.SnowBoberGame;
import snowbober.Util.Game;

import java.awt.*;
import java.util.ArrayList;

public class GameScreen implements Screen {

    private SnowBoberGame game;
    public World world;

    private Camera camera;
    private Viewport viewport;

    private SpriteBatch batch;
    private Texture background;

    private int backgroundOffset;
    public long frame;
    public boolean gameOver;
    public boolean gameOverInit = true;

    public GameScreen(World world, SpriteBatch batch) {
        this.world = world;
        camera = new OrthographicCamera();
        viewport = new FitViewport(SnowBoberGame.V_WIDTH, SnowBoberGame.V_HEIGHT, camera);
        background = new Texture("background.jpg");
        backgroundOffset = 0;
        this.batch = batch;
        gameOver = false;
    }

    @Override
    public void show() {

    }

    @Override
    public void render(float delta) {
// todo dodac delte do move system
        try {
            if (gameOver && gameOverInit) {
//                world.killAllEntities();
//                world.removeAllSystems();
                if (gameOverInit) {
                    world.addComponentToEntity(15, new Visual(new Texture("game-over.jpg"), SnowBoberGame.V_WIDTH, SnowBoberGame.V_HEIGHT));
                    world.addComponentToEntity(15, new Position(0, 0));
                }
                gameOverInit = false;
            } else if (!gameOver && !gameOverInit) {
                gameOverInit = true;

            } else {
                world.update(frame++, delta);
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void resize(int width, int height) {
        viewport.update(width, height, true);
        batch.setProjectionMatrix(camera.combined);
    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose() {

    }
}
