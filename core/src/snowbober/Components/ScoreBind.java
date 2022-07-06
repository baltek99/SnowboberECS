package snowbober.Components;

import snowbober.ECS.ComponentWithId;
import snowbober.Enums.CmpId;

public class ScoreBind extends ComponentWithId {
    public int playerId;

    public ScoreBind(int playerId) {
        super(CmpId.SCORE_BIND.ordinal());
        this.playerId = playerId;
    }
}
