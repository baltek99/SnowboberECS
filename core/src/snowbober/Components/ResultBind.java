package snowbober.Components;

import snowbober.ECS.ComponentWithId;
import snowbober.Enums.CmpId;

import java.io.Serializable;

public class ResultBind extends ComponentWithId implements Serializable, Comparable<ResultBind> {
    public final String name;
    public final int score;

    public ResultBind(String name, int score) {
        super(CmpId.RESULT_BIND.ordinal());
        this.name = name;
        this.score = score;
    }

    @Override
    public int compareTo(ResultBind result) {
        return Integer.compare(this.score, result.score);
    }

    @Override
    public String toString() {
        return name + " : " + score;
    }
}
