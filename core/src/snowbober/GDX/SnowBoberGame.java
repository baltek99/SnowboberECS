package snowbober.GDX;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import snowbober.Components.*;
import snowbober.ECS.World;
import snowbober.GDX.Screens.GameScreen;
import snowbober.Systems.*;
import snowbober.Util.Util;

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

    void createWorld() {

        world.addSystem(new MoveSystem());
        world.addSystem(new BackgroundGeneratorSystem(V_WIDTH, V_HEIGHT));
        world.addSystem(new ObstacleGeneratorSystem(5, 10, V_WIDTH, V_HEIGHT));
        world.addSystem(new JumpSystem());
        world.addSystem(new CollisionSystem(batch));
        world.addSystem(new PlayerCollisionSystem());
        world.addSystem(new RenderSystem(batch));
        world.addSystem(new GameOverSystem(gameScreen));

        int background1 = 0;
        Texture backgroundTexture = new Texture("background.jpg");
        world.addComponentToEntity(background1, new Position(0, 0));
        world.addComponentToEntity(background1, new Visual(backgroundTexture, V_WIDTH, V_HEIGHT));
        world.addComponentToEntity(background1, new Move(-1));

        int background2 = 1;
        Texture background2Texture = new Texture("background.jpg");
        world.addComponentToEntity(background2, new Position(V_WIDTH, 0));
        world.addComponentToEntity(background2, new Visual(background2Texture, V_WIDTH, V_HEIGHT));
        world.addComponentToEntity(background2, new Move(-1));

        int player = 3;
        Texture playerTexture = new Texture("bober-stand.png");
        world.addComponentToEntity(player, new Position(100, 70));
        world.addComponentToEntity(player, new Jump());
        world.addComponentToEntity(player, new PlayerControlled(PlayerState.IDLE));
        world.addComponentToEntity(player, new Collision(180, 215, 100, ObstacleType.PLAYER));
        world.addComponentToEntity(player, new Visual(playerTexture, 180, 215));
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
