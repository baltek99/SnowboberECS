package snowbober.GDX.Screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import snowbober.Components.*;
import snowbober.ECS.GameState;
import snowbober.ECS.World;
import snowbober.GDX.SnowBoberGame;
import snowbober.Systems.*;
import snowbober.Util.ConstVals;

public class GameScreen implements Screen {

    public static final int V_WIDTH = ConstVals.V_WIDTH;
    public static final int V_HEIGHT = ConstVals.V_HEIGHT;

    public World mainMenuECS;
    public World gameplayECS;
    public World gameOverECS;

    private final Camera camera;
    private final Viewport viewport;
    private final SpriteBatch batch;

    public GameState state;
    public long frame;
    public boolean gameOver;

    public GameScreen() {
        this.batch = new SpriteBatch();
        camera = new OrthographicCamera();
        viewport = new FitViewport(SnowBoberGame.V_WIDTH, SnowBoberGame.V_HEIGHT, camera);
        gameOver = false;
        state = GameState.MAIN_MENU;

        mainMenuECS = createStartWorld();
//        gameplayECS = createGameWorld();
        gameOverECS = createGameOverWorld();
    }

    public World createGameWorld() {
        World world = new World();

        world.addSystem(new MoveSystem());
        world.addSystem(new BackgroundGeneratorSystem(V_WIDTH, V_HEIGHT));
        world.addSystem(new ObstacleGeneratorSystem(5, 8, V_WIDTH, V_HEIGHT));
        world.addSystem(new PlayerControlledSystem());
        world.addSystem(new CollisionSystem(batch));
        world.addSystem(new PlayerCollisionSystem());
        world.addSystem(new JumpOnRailSystem());
        world.addSystem(new JumpSystem());
        world.addSystem(new RailSystem());
        world.addSystem(new SpeedSystem());
        world.addSystem(new GameOverSystem(this));

        world.addRenderSystem(new RenderSystem(batch));
        world.addRenderSystem(new ScoreRenderSystem(batch));

        int background1 = 0;
        Texture backgroundTexture = new Texture("background.jpg");
        world.addComponentToEntity(background1, new Position(0, 0));
        world.addComponentToEntity(background1, new Visual(backgroundTexture, V_WIDTH, V_HEIGHT));
        world.addComponentToEntity(background1, new Move(-2));

        int background2 = 1;
        Texture background2Texture = new Texture("background.jpg");
        world.addComponentToEntity(background2, new Position(V_WIDTH, 0));
        world.addComponentToEntity(background2, new Visual(background2Texture, V_WIDTH, V_HEIGHT));
        world.addComponentToEntity(background2, new Move(-2));

        int scoreLabel = 2;
        world.addComponentToEntity(scoreLabel, new Position(V_WIDTH - 200, 50));
        world.addComponentToEntity(scoreLabel, new ScoreBind(15));

        int player = 15;
        Texture playerTexture = new Texture("bober-stand.png");
        world.addComponentToEntity(player, new Position(ConstVals.BOBER_DEFAULT_POSITION_X, ConstVals.BOBER_DEFAULT_POSITION_Y));
        world.addComponentToEntity(player, new Jump());
        world.addComponentToEntity(player, new PlayerControlled(PlayerState.IDLE));
        world.addComponentToEntity(player, new Collision(ConstVals.BOBER_DEFAULT_WIDTH, ConstVals.BOBER_DEFAULT_HEIGHT, ObstacleType.PLAYER));
        world.addComponentToEntity(player, new Visual(playerTexture, ConstVals.BOBER_DEFAULT_WIDTH, ConstVals.BOBER_DEFAULT_HEIGHT));
        world.addComponentToEntity(player, new Score(0));

        return world;
    }

    public World createGameOverWorld() {
        World world = new World();

        world.addRenderSystem(new RenderSystem(batch));

        int background = 0;
        world.addComponentToEntity(background, new Visual(new Texture("game-over.jpg"), SnowBoberGame.V_WIDTH, SnowBoberGame.V_HEIGHT));
        world.addComponentToEntity(background, new Position(0, 0));

        return world;
    }

    public World createStartWorld() {
        World world = new World();

        world.addRenderSystem(new RenderSystem(batch));

        int background = 0;
        world.addComponentToEntity(background, new Visual(new Texture("game-over.jpg"), SnowBoberGame.V_WIDTH, SnowBoberGame.V_HEIGHT));
        world.addComponentToEntity(background, new Position(0, 0));

        return world;
    }


    @Override
    public void show() {

    }

    @Override
    public void render(float delta) {
// todo dodac delte do move system
        try {
            state = updateState(state, frame, delta);
            frame++;
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private GameState updateState(GameState state, long frame, float delta) throws InterruptedException {
        switch (state) {
            case MAIN_MENU:
                if (Gdx.input.isKeyJustPressed(Input.Keys.SPACE)) {
                    gameplayECS = createGameWorld();
                    gameOver = false;
                    return GameState.GAMEPLAY;
                }
                mainMenuECS.updateSystems(frame, delta);
                mainMenuECS.updateRenderSystems(frame, delta);
                return state;
            case GAMEPLAY:
                if (gameOver) {
//                    gameplayECS.resetWorld();
                    return GameState.GAME_OVER;
                }
                gameplayECS.updateSystems(frame, delta);
                gameplayECS.updateRenderSystems(frame, delta);
                return state;
            case GAME_OVER:
                if (Gdx.input.isKeyJustPressed(Input.Keys.SPACE)) {
                    return GameState.MAIN_MENU;
                }
                gameOverECS.updateSystems(frame, delta);
                gameOverECS.updateRenderSystems(frame, delta);
                return state;
        }
        return null;
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
