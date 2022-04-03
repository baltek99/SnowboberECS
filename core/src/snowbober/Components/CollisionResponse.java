package snowbober.Components;

import snowbober.ECS.ComponentWithId;
import snowbober.Enums.CmpId;
import snowbober.Enums.CollisionType;
import snowbober.Enums.ObstacleType;

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
