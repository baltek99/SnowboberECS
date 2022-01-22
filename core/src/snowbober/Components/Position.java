package snowbober.Components;

import snowbober.ECS.ComponentWithId;

public class Position extends ComponentWithId {
    public int x, y;

    public Position(int x, int y) {
        super(CmpId.POSITION.ordinal());
        this.x = x;
        this.y = y;
    }
}
