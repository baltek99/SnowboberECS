package snowbober.Components;

import com.badlogic.gdx.graphics.Texture;
import snowbober.ECS.ComponentWithId;
//import snowbober.Util.Texture;

public class Visual extends ComponentWithId {
    public Texture texture;
    public int imgWidth;
    public int imgHeight;

    public Visual(Texture texture, int width, int height) {
        super(CmpId.VISUAL.ordinal());
        this.texture = texture;
        imgWidth = width;
        imgHeight = height;
    }
}
