package snowbober.GDX.Screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.google.gson.Gson;
import snowbober.Components.*;
import snowbober.ECS.World;
import snowbober.Enums.CmpId;
import snowbober.Enums.GameState;
import snowbober.Enums.ObstacleType;
import snowbober.Enums.PlayerState;
import snowbober.Systems.*;
import snowbober.Util.ConstValues;
import snowbober.Util.HighScores;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

public class GameScreen implements Screen {

    public static final int V_WIDTH = ConstValues.V_WIDTH;
    public static final int V_HEIGHT = ConstValues.V_HEIGHT;

    public World mainMenuECS;
    public World gameplayECS;
    public World gameOverECS;
    public World highScoresECS;

    private final Camera camera;
    private final Viewport viewport;
    private SpriteBatch batch;

    private final Music gameplayMusic;

    public GameState state;
    public long frame;
    public boolean gameOver;
    public boolean isNameGiven;
    public int playerResult;
    private String playerName;

    private HighScores highScores;
    private final String highScoresPath = "highscores.json";
    private final Gson gson;

    public GameScreen() {
        this.batch = new SpriteBatch();
        camera = new OrthographicCamera();
        viewport = new FitViewport(V_WIDTH, V_HEIGHT, camera);
        gameOver = false;
        state = GameState.MAIN_MENU;

        isNameGiven = false;
        playerName = "player";

        highScores = new HighScores();
        gson = new Gson();
        try {
            FileReader fileReader = new FileReader(highScoresPath);
            highScores = gson.fromJson(fileReader, HighScores.class);

        } catch (FileNotFoundException e) {
            System.out.println("HighScores file not found!");
            e.printStackTrace();
        }

        gameplayMusic = Gdx.audio.newMusic(Gdx.files.internal("boberMusic.wav"));
        gameplayMusic.setLooping(true);
        gameplayMusic.setVolume(0.5f);

        mainMenuECS = createStartWorld();
    }

    public World createGameWorld(String playerName) {
        batch.dispose();
        batch = new SpriteBatch();
        batch.setProjectionMatrix(camera.combined);

        gameplayMusic.play();

        World world = new World();
        frame = 0;

        world.addSystem(new MoveSystem());
        world.addSystem(new BackgroundGeneratorSystem(V_WIDTH, V_HEIGHT));
        world.addSystem(new ObstacleGeneratorSystem(3, 12, 7, 4));
        world.addSystem(new PlayerControlledSystem());
        world.addSystem(new JumpOnRailSystem());
        world.addSystem(new CollisionSystem());
        world.addSystem(new PlayerCollisionSystem(this));
        world.addSystem(new JumpSystem());
        world.addSystem(new RailSystem());
        world.addSystem(new SpeedSystem());
        world.addSystem(new ImmortalSystem());
        world.addSystem(new GameOverSystem(this));

        world.addRenderSystem(new RenderSystem(batch));
        world.addRenderSystem(new ScoreRenderSystem(batch));

        // 0-1 - backgrounds / 2 - scoreLabel / 3-6 - obstacles / 7-10 - score / 11 - player / 12-15 - grid / 16-18 - lives /

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
        world.addComponentToEntity(scoreLabel, new Position(ConstValues.SCORE_POSITION_X, ConstValues.SCORE_POSITION_Y));
        world.addComponentToEntity(scoreLabel, new ScoreBind(11));

        int life1 = 16;
        Texture lifeTexture = new Texture("heart.png");
        world.addComponentToEntity(life1, new Position(ConstValues.HEART_POSITION_X_1, ConstValues.HEART_POSITION_Y));
        world.addComponentToEntity(life1, new Visual(lifeTexture, ConstValues.HEART_WIDTH, ConstValues.HEART_HEIGHT));

        int life2 = 17;
        world.addComponentToEntity(life2, new Position(ConstValues.HEART_POSITION_X_2, ConstValues.HEART_POSITION_Y));
        world.addComponentToEntity(life2, new Visual(lifeTexture, ConstValues.HEART_WIDTH, ConstValues.HEART_HEIGHT));

        int life3 = 18;
        world.addComponentToEntity(life3, new Position(ConstValues.HEART_POSITION_X_3, ConstValues.HEART_POSITION_Y));
        world.addComponentToEntity(life3, new Visual(lifeTexture, ConstValues.HEART_WIDTH, ConstValues.HEART_HEIGHT));

        Queue<Integer> lives = new LinkedList<>();
        lives.add(16);
        lives.add(17);
        lives.add(18);

        int player = 11;
        Texture playerTexture = new Texture("bober-stand.png");
        world.addComponentToEntity(player, new Position(ConstValues.BOBER_DEFAULT_POSITION_X, ConstValues.BOBER_DEFAULT_POSITION_Y));
        world.addComponentToEntity(player, new Jump());
        world.addComponentToEntity(player, new PlayerControlled(PlayerState.IDLE, playerName));
        world.addComponentToEntity(player, new Collision(ConstValues.BOBER_DEFAULT_WIDTH, ConstValues.BOBER_DEFAULT_HEIGHT, ObstacleType.PLAYER));
        world.addComponentToEntity(player, new Visual(playerTexture, ConstValues.BOBER_DEFAULT_WIDTH, ConstValues.BOBER_DEFAULT_HEIGHT));
        world.addComponentToEntity(player, new Score(0));
        world.addComponentToEntity(player, new Lives(lives));

        return world;
    }

    public World createGameOverWorld() {
        gameplayMusic.stop();
        highScores.addResult(playerName, playerResult);

        try {
            FileWriter writer = new FileWriter(highScoresPath);
            gson.toJson(highScores, writer);
            writer.flush();
            writer.close();
        } catch (IOException e) {
            System.out.println("HighScores object serialization failed!");
            e.printStackTrace();
        }

        World world = new World();

        world.addRenderSystem(new RenderSystem(batch));
        world.addRenderSystem(new ResultRenderSystem(batch));

        int background = 0;
        world.addComponentToEntity(background, new Visual(new Texture("game-over.jpg"), V_WIDTH, V_HEIGHT));
        world.addComponentToEntity(background, new Position(0, 0));

        int result = 1;
        world.addComponentToEntity(result, new TextField(playerName));
        world.addComponentToEntity(result, new Score(playerResult));
        world.addComponentToEntity(result, new Position(200, 50));

        return world;
    }

    public World createStartWorld() {
        World world = new World();

        world.addSystem(new TextInputSystem(world, this));
        world.addRenderSystem(new RenderSystem(batch));

        int background = 0;
        world.addComponentToEntity(background, new Visual(new Texture("start.jpg"), V_WIDTH, V_HEIGHT));
        world.addComponentToEntity(background, new Position(0, 0));

        int textInput = 1;
        world.addComponentToEntity(textInput, new TextField());

        return world;
    }

    public World createHighScoreWorld() {
        World world = new World();

        world.addRenderSystem(new RenderSystem(batch));
        world.addRenderSystem(new HighScoresRenderSystem(batch));

        int background = 0;
        Texture backgroundTexture = new Texture("high-scores.jpg");
        world.addComponentToEntity(background, new Position(0, 0));
        world.addComponentToEntity(background, new Visual(backgroundTexture, V_WIDTH, V_HEIGHT));

        List<ResultBind> scores = highScores.getScores();
        if (scores.size() > 0) {
            int inc = V_HEIGHT / scores.size();

            for (int i = scores.size() - 1; i >= 0; i--) {
                world.addComponentToEntity(i + 1, scores.get(i));
                world.addComponentToEntity(i + 1, new Score(scores.size() - i));
                world.addComponentToEntity(i + 1, new Position(300, (i + 1) * inc - 25));
            }
        }

        return world;
    }

    @Override
    public void show() {

    }

    @Override
    public void render(float delta) {
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
                if (Gdx.input.isKeyJustPressed(Input.Keys.H) || Gdx.input.isKeyJustPressed(Input.Keys.TAB)) {
                    highScoresECS = createHighScoreWorld();
                    return GameState.HIGH_SCORE;
                }
                if (isNameGiven) {
                    isNameGiven = false;
                    playerName = ((TextField) mainMenuECS.getComponent(1, CmpId.TEXT_FIELD.ordinal())).text;
                    gameplayECS = createGameWorld(playerName);
                    java.lang.System.out.println(playerName);
                    gameOver = false;
                    return GameState.GAMEPLAY;
                }
                if (frame % 2 == 0) {
                    mainMenuECS.updateSystems(frame, delta);
                }
                mainMenuECS.updateRenderSystems(frame, delta);

                return state;
            case GAMEPLAY:
                if (Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE)) {
                    gameplayMusic.pause();
                    return GameState.PAUSE;
                }
                if (gameOver) {
                    gameOverECS = createGameOverWorld();
                    return GameState.GAME_OVER;
                }
                gameplayECS.updateSystems(frame, delta);
                gameplayECS.updateRenderSystems(frame, delta);
                return state;
            case GAME_OVER:
                if (Gdx.input.isKeyJustPressed(Input.Keys.H) || Gdx.input.isKeyJustPressed(Input.Keys.TAB)) {
                    highScoresECS = createHighScoreWorld();
                    gameOver = false;
                    return GameState.HIGH_SCORE;
                }
                if (Gdx.input.isKeyJustPressed(Input.Keys.ANY_KEY)) {
                    gameplayECS = createGameWorld(playerName);
                    gameOver = false;
                    return GameState.GAMEPLAY;
                }
                gameOverECS.updateSystems(frame, delta);
                gameOverECS.updateRenderSystems(frame, delta);
                return state;
            case HIGH_SCORE:
                if (Gdx.input.isKeyJustPressed(Input.Keys.ANY_KEY)) {
                    return GameState.MAIN_MENU;
                }
                highScoresECS.updateRenderSystems(frame, delta);
                return state;
            case PAUSE:
                if (Gdx.input.isKeyJustPressed(Input.Keys.ENTER)) {
                    gameplayMusic.play();
                    return GameState.GAMEPLAY;
                }
                if (Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE)) {
                    gameplayMusic.stop();
                    mainMenuECS = createStartWorld();
                    return GameState.MAIN_MENU;
                }
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
