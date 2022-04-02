package snowbober.Components;

import snowbober.ECS.ComponentWithId;

public class PlayerControlled extends ComponentWithId {

    public PlayerState playerState;
    public String name;

    public PlayerControlled(PlayerState state, String name) {
        super(CmpId.PLAYER_CONTROLLED.ordinal());
        playerState = state;
        this.name = name;
    }

}
