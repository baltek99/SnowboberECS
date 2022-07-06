package snowbober.Systems;

import com.badlogic.gdx.graphics.Texture;
import snowbober.Components.*;
import snowbober.ECS.Component;
import snowbober.ECS.System;
import snowbober.ECS.World;
import snowbober.Enums.CmpId;
import snowbober.Enums.ObstacleType;
import snowbober.Enums.PlayerState;
import snowbober.GDX.Screens.GameScreen;
import snowbober.Util.ConstValues;

import java.util.ArrayList;

public class PlayerCollisionSystem implements System {
    private final GameScreen gameScreen;

    public PlayerCollisionSystem(GameScreen gameScreen) {
        this.gameScreen = gameScreen;
    }

    @Override
    public void update(long gameFrame, float delta, World world) {
        ArrayList<Component[]> components = world.getEntitiesWithComponents(new int[]{
                CmpId.PLAYER_CONTROLLED.ordinal(),
                CmpId.COLLISION_RESPONSE.ordinal(),
                CmpId.POSITION.ordinal(),
                CmpId.VISUAL.ordinal(),
                CmpId.SCORE.ordinal(),
                CmpId.LIVES.ordinal()
        });

        for (int entity = 0; entity < world.MAX_ENTITIES; entity++) {
            if (!World.isEntityOk(entity, components)) continue;

            PlayerControlled pc = (PlayerControlled) components.get(0)[entity];
            CollisionResponse cr = (CollisionResponse) components.get(1)[entity];
            Position pos = (Position) components.get(2)[entity];
            Score score = (Score) components.get(4)[entity];
            Lives liv = (Lives) components.get(5)[entity];

            if (cr.obstacle == ObstacleType.SCORE_POINT) {
                score.score++;
                world.killEntity(cr.collidingEntityId);
                world.removeComponentFromEntity(entity, cr);
            } else if (cr.obstacle == ObstacleType.BOX || (cr.obstacle == ObstacleType.RAIL &&
                    (pc.playerState == PlayerState.IDLE || pc.playerState == PlayerState.CROUCH))) {
                removeLifeOrKill(world, entity, liv, score.score);
                pos.y = ConstValues.BOBER_DEFAULT_POSITION_Y;
                if (cr.obstacle == ObstacleType.BOX) {
                    components.get(3)[cr.collidingEntityId] = new Visual(new Texture("box-broken.png"), ConstValues.BOX_WIDTH, ConstValues.BOX_HEIGHT);
                }
            } else if (cr.obstacle == ObstacleType.RAIL && (pc.playerState == PlayerState.JUMPING ||
                    pc.playerState == PlayerState.JUMPING_FROM_CROUCH || pc.playerState == PlayerState.JUMPING_ON_RAIL)) {
                pos.y = ConstValues.SLIDING_ON_RAIL_Y;
                pc.playerState = PlayerState.SLIDING;
                Texture texture = new Texture("bober-rail.png");
                components.get(3)[entity] = new Visual(texture, ConstValues.BOBER_ON_RAIL_WIDTH, ConstValues.BOBER_ON_RAIL_HEIGHT);
                world.removeComponentFromEntity(entity, cr);
            } else if (cr.obstacle == ObstacleType.GRID) {
                if (pc.playerState != PlayerState.CROUCH) {
                    removeLifeOrKill(world, entity, liv, score.score);
                    pos.y = ConstValues.BOBER_DEFAULT_POSITION_Y;
                    components.get(3)[cr.collidingEntityId - 9] = new Visual(new Texture("grid-broken.png"), ConstValues.GRID_WIDTH, ConstValues.GRID_HEIGHT);
                    world.killEntity(cr.collidingEntityId);
                } else {
                    world.removeComponentFromEntity(entity, cr);
                }
            }
        }
    }

    private void removeLifeOrKill(World world, int entity, Lives liv, int score) {
        if (liv.livesIds.size() == 1) {
            gameScreen.playerResult = score;
            world.killEntity(entity);
            world.killEntity(liv.livesIds.poll());
        } else {
            int lifeID = liv.livesIds.poll();
            world.killEntity(lifeID);
            world.removeComponentFromEntity(entity, CmpId.COLLISION.ordinal());
            world.removeComponentFromEntity(entity, CmpId.COLLISION_RESPONSE.ordinal());
        }
    }
}
