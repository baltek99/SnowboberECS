package snowbober.Util;

import javax.imageio.ImageIO;
import java.io.File;
import java.io.IOException;

public class Util {
    public static Texture loadImage(String path, float scale) {
        Texture texture = new Texture();
        File imageFile = new File(path);
        try {
            texture.img = ImageIO.read(imageFile);
            texture.imgWidth = (int) (texture.img.getWidth() * scale);
            texture.imgHeight = (int) (texture.img.getHeight() * scale);
        } catch (IOException e) {
            System.err.println("Błąd odczytu tekstury");
            e.printStackTrace();
        }
        return texture;
    }

    /**
     * Linearly interpolates a value between two floats
     *
     * @param start_value Start value
     * @param end_value   End value
     * @param pct         Our progress or percentage. [0,1]
     * @return Interpolated value between two floats
     */
    public static float lerp(float start_value, float end_value, float pct) {
        return (start_value + (end_value - start_value) * pct);
    }

    public static float easeOut(float x) {
        return 1 - (float) Math.pow(1 - x, 3);
    }

    public static float flip(float x) {
        return 1 - x;
    }

    public static float spike(float x) {
        if (x <= 0.5) {
            return easeOut(x / 0.5f);
        }
        return easeOut(flip(x) / 0.5f);
    }
}
