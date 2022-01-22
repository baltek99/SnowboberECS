package snowbober.Components;

import snowbober.ECS.ComponentWithId;

public class PlayerControlled extends ComponentWithId {

    public PlayerState playerState;

    public PlayerControlled(PlayerState state) {
        super(CmpId.PLAYER_CONTROLLED.ordinal());
        playerState = state;
    }

}
