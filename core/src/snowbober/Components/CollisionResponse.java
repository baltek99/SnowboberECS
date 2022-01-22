package snowbober.Components;

import snowbober.ECS.ComponentWithId;

public class CollisionResponse extends ComponentWithId {
    public int collidingEntityId;
    public CollisionType type;
    public ObstacleType obstacle;

    public CollisionResponse(int collidingEntityId, CollisionType type, ObstacleType obs) {
        super(CmpId.COLLISION_RESPONSE.ordinal());
        this.collidingEntityId = collidingEntityId;
        this.type = type;
        obstacle = obs;
    }
}
