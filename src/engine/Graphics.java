package engine;

import java.nio.IntBuffer;

/**
 * Created by Zeejfps on 4/21/2017.
 */
public class Graphics {

    private final int resolutionX, resolutionY;
    private final IntBuffer pixels;

    public Graphics(int resolutionX, int resolutionY, IntBuffer pixels) {
        this.resolutionX = resolutionX;
        this.resolutionY = resolutionY;
        this.pixels = pixels;
    }

    public int width() {
        return resolutionX;
    }

    public int height() {
        return resolutionY;
    }

    public void fillRect(int x, int y, int w, int h, int color) {

        int xs = x < 0 ? 0 : x;
        int ys = y < 0 ? 0 : y;

        int xe = x+w;
        if (xe > resolutionX) xe = resolutionX;

        int ye = y+h;
        if (ye > resolutionY) ye = resolutionY;

        for (int i = ys; i < ye; i++) {
            int yy = i*resolutionX;
            for (int j = xs; j < xe; j++) {
                pixels.put(j+yy, color);
            }
        }

    }

}
