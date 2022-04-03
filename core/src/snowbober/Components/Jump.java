package snowbober.Components;

import snowbober.ECS.ComponentWithId;
import snowbober.Enums.CmpId;

public class Jump extends ComponentWithId {
    public float startJumpFrame;
    public int jumpFrom;

    public Jump() {
        super(CmpId.JUMP.ordinal());
    }
}
