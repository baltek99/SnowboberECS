package snowbober.Components;

import snowbober.ECS.ComponentWithId;

public class Score extends ComponentWithId {

    public int score;

    public Score(int score) {
        super(CmpId.SCORE.ordinal());
        this.score = score;
    }
}
