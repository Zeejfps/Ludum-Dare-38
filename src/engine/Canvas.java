package engine;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;
import org.lwjgl.opengl.GL30;
import org.lwjgl.system.MemoryUtil;

import java.nio.IntBuffer;

/**
 * Created by Zeejfps on 4/21/2017.
 */
public class Canvas {

    // framebuffer texture and id
    final int texture;
    final int framebuffer;

    // the pixels of the canvas
    final IntBuffer pixels;

    // graphics object that does the drawing
    private final Graphics graphics;
    private final int width, height;

    public Canvas(int width, int height) {
        this.width = width;
        this.height = height;

        pixels = MemoryUtil.memAllocInt(width*height);

        framebuffer = GL30.glGenFramebuffers();
        GL30.glBindFramebuffer(GL30.GL_FRAMEBUFFER, framebuffer);

        texture = GL11.glGenTextures();
        GL11.glBindTexture(GL11.GL_TEXTURE_2D, texture);

        GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MAG_FILTER, GL11.GL_NEAREST);
        GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER, GL11.GL_NEAREST);
        GL11.glTexImage2D(GL11.GL_TEXTURE_2D, 0, GL11.GL_RGB8, width, height, 0, GL12.GL_BGRA, GL11.GL_UNSIGNED_BYTE, 0);

        GL30.glFramebufferTexture2D(GL30.GL_FRAMEBUFFER, GL30.GL_COLOR_ATTACHMENT0, GL11.GL_TEXTURE_2D, texture, 0);
        GL30.glBindFramebuffer(GL30.GL_FRAMEBUFFER, 0);

        graphics = new Graphics(width, height, pixels);
    }

    public int width() {
        return width;
    }

    public int height() {
        return height;
    }

    public Graphics getGraphics() {
        return graphics;
    }
}
