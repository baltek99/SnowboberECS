package snowbober.Components;

import snowbober.ECS.ComponentWithId;
import snowbober.Enums.CmpId;

public class TextField extends ComponentWithId {

    public String text;

    public TextField() {
        super(CmpId.TEXT_FIELD.ordinal());
    }

    public TextField(String text) {
        super(CmpId.TEXT_FIELD.ordinal());
        this.text = text;
    }
}
