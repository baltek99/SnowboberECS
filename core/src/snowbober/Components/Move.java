package snowbober.Components;

import snowbober.ECS.ComponentWithId;
import snowbober.Enums.CmpId;

public class Move extends ComponentWithId {
    public float speed;

    public Move(float speed) {
        super(CmpId.MOVE.ordinal());
        this.speed = speed;
    }
}
