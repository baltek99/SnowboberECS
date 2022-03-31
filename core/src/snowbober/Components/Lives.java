package snowbober.Components;

import snowbober.ECS.ComponentWithId;

import java.util.Queue;

public class Lives extends ComponentWithId {

    public int lives;
    public Queue<Integer> livesIds;

    public Lives(Queue<Integer> livesIds) {
        super(CmpId.LIVES.ordinal());
        this.lives = livesIds.size();
        this.livesIds = livesIds;
    }
}

