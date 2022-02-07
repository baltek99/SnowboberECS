package snowbober.Util;

import snowbober.Components.*;
import snowbober.ECS.World;
import snowbober.Systems.*;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.LinkedList;
import java.util.Queue;

public class Game implements ActionListener {

    int winWidth, winHeight;
    String winTitle;
    public JFrame winFrame;

    public Renderer renderer;
    public Queue<InputActions> inputActions;

    World world = new World();
    public static boolean gameOver = false;
    public static boolean initGameOver = false;
    public static int score;
    public static boolean resetWorld = false;

    public Game() {
        initWindow();
        renderer = new Renderer(world);
        inputActions = new LinkedList<>();
        winFrame.add(renderer);
        winFrame.setVisible(true);

        createWorld();
    }

    void createWorld() {
        gameOver = false;

//        world.addSystem(new PlayerControlledSystem(inputActions));
        world.addSystem(new JumpSystem());
        world.addSystem(new MoveSystem());
        world.addSystem(new ObstacleGeneratorSystem(5, 10, 100, 100));
        world.addSystem(new BackgroundGeneratorSystem(0, 1));
//        world.addSystem(new CollisionSystem());
        world.addSystem(new PlayerCollisionSystem());
        world.addSystem(new RailSystem());
//        world.addSystem(new GameOverSystem());

        int background = 0;
        Texture bckg = Util.loadImage("assets/tlo-kolor.jpg", 0.8f);
        world.addComponentToEntity(background, new Position(0, 0));
        //world.addComponentToEntity(background, new Visual(bckg));
        world.addComponentToEntity(background, new Move(-1));

        int background2 = 1;
        world.addComponentToEntity(background2, new Position(1536, 0));
        //world.addComponentToEntity(background2, new Visual(bckg));
        world.addComponentToEntity(background2, new Move(-1));

        int player = 14;
        Texture t = Util.loadImage("assets/bober-stand.png", 0.7f);
        world.addComponentToEntity(player, new Position(200, 400));
        world.addComponentToEntity(player, new Jump());
        world.addComponentToEntity(player, new PlayerControlled(PlayerState.IDLE));
//        world.addComponentToEntity(player, new Collision(80, ObstacleType.PLAYER));
        //world.addComponentToEntity(player, new Visual(t));
    }

    void initWindow() {
        this.winWidth = 1500;
        this.winHeight = 800;
        this.winTitle = "SnowBober";
        winFrame = new JFrame();
        winFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        winFrame.setSize(winWidth, winHeight);
        winFrame.setTitle(winTitle);
        winFrame.setResizable(false);

        winFrame.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (gameOver) {
                    score = 0;
                    System.out.println("World reset");
                    resetWorld = true;
                } else {
                    if (e.getKeyChar() == ' ') {
                        inputActions.add(InputActions.JUMP);
                    }
                }
            }
        });


    }

    @Override
    public void actionPerformed(ActionEvent e) {
    }

    public static void main(String[] args) throws InterruptedException {
        Game game = new Game();

        long gameFrame = 0;
        int time = 8;
        while (true) {

            if (gameOver == true && initGameOver == true) {
                System.out.println("Game Over");
                game.world.killAllEntities();
                game.world.removeAllSystems();
                //game.world.addComponentToEntity(15, new Visual(Util.loadImage("assets/game-over.jpg", 0.8f)));
                game.world.addComponentToEntity(15, new Position(0, 0));
                initGameOver = false;
            } else if (resetWorld) {
                game.world.killAllEntities();
                resetWorld = false;
                game.createWorld();
                time = 8;
            }

//            game.world.update(gameFrame);

            game.renderer.repaint();

            if (gameFrame % 1000 == 0) time -= 0.5;
            if (time <= 2) time = 2;
            Thread.sleep(time);
            gameFrame += 1;
            //System.out.println("Score: " + score);
        }
    }
}






/*
interface State
{
    void onEnter();
    void onUpdate(int gameFrame, StateManager manager);
    void onExit();
}

class GameplayState implements State
{
    void onEnter()
    {
    }
    void onUpdate(int gameFrame; StateManager manager)
    {
        // somewhere in here
        // stateManager.requestState(GameOverStateId);
    }
    void onExit()
    {
    }
}

class GemoverState implements State
{
    void onEnter()
    {
    }
    void onUpdate(int gameFrame; StateManager manager)
    {
        // somewhere in here
        // stateManager.requestState(GameOverStateId);
    }
    void onExit()
    {
    }
}

class StateManager
{
    State currentState;
    State nextState;

    void update(int gameFrame)
    {
        if (nextState)
        {
            changeSate(nextState);
        }
        currentState.update(gameFrame);
    }

    void changeState(State nextState)
    {
        currentState.onExit();
        currentState = nextState;
        currentState.onEnter();
        nextState = null;
    }

    void requestState(State state)
    {
        nextState = state;
    }
}

 */