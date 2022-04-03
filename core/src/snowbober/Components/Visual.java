package snowbober.Components;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import snowbober.ECS.ComponentWithId;
import snowbober.Enums.CmpId;
//import snowbober.Util.Texture;

public class Visual extends ComponentWithId {
    public TextureRegion texture;
    public int imgWidth;
    public int imgHeight;
    public float rotation;

    public Visual(Texture texture, int width, int height) {
        super(CmpId.VISUAL.ordinal());
        this.texture = new TextureRegion(texture);
        imgWidth = width;
        imgHeight = height;
        rotation = 0;
    }
}
