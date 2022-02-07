package snowbober.GDX;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import snowbober.Components.*;
import snowbober.ECS.World;
import snowbober.GDX.Screens.GameScreen;
import snowbober.Systems.*;
import snowbober.Util.ConstVals;

public class SnowBoberGame extends Game {

    private GameScreen gameScreen;
    private World world;
    private SpriteBatch batch;

    public static final int V_WIDTH = 964;
    public static final int V_HEIGHT = 540;

    @Override
    public void create() {
        world = new World();
        batch = new SpriteBatch();
        gameScreen = new GameScreen(world, batch);
        createWorld();
        setScreen(gameScreen);
    }

    public void createWorld() {

        world.addSystem(new MoveSystem());
        world.addSystem(new BackgroundGeneratorSystem(V_WIDTH, V_HEIGHT));
        world.addSystem(new ObstacleGeneratorSystem(5, 7, V_WIDTH, V_HEIGHT));
        world.addSystem(new PlayerControlledSystem());
        world.addSystem(new CollisionSystem(batch));
        world.addSystem(new PlayerCollisionSystem());
        world.addSystem(new JumpOnRailSystem());
        world.addSystem(new JumpSystem());
        world.addSystem(new RailSystem());
        world.addSystem(new SpeedSystem());
        world.addSystem(new GameOverSystem(gameScreen));

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
    }

    @Override
    public void render() {
        super.render();
    }

    @Override
    public void dispose() {
//        super.dispose();
        gameScreen.dispose();
    }

    @Override
    public void resize(int width, int height) {
        super.resize(width, height);
    }
}
