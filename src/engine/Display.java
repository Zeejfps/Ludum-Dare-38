package engine;

import org.lwjgl.glfw.GLFWVidMode;
import org.lwjgl.glfw.GLFWWindowSizeCallback;
import org.lwjgl.opengl.GL;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;
import org.lwjgl.opengl.GL30;
import org.lwjgl.system.MemoryUtil;

import java.nio.IntBuffer;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.system.MemoryUtil.NULL;

/**
 * Created by Zeejfps on 10/29/2016.
 */
public class Display {

    // glfw window handle
    private final long window;
    private int windowWidth, windowHeight;

    // framebuffer texture and id
    private final int texture;
    private final int framebuffer;
    private final int resolutionX, resolutionY;
    private int fPosXS, fPosYS, fPosXE, fPosYE;
    private double aspect;

    // the pixels of the display
    private final IntBuffer pixels;

    // graphics object that does the drawing
    private final Graphics graphics;

    public Display(int windowWidth, int windowHeight,
                   int resolutionX, int resolutionY,
                   String windowTitle) {

        this.windowWidth = windowWidth;
        this.windowHeight = windowHeight;
        this.resolutionX = resolutionX;
        this.resolutionY = resolutionY;
        this.aspect = (double)resolutionX / resolutionY;

        // set the hints
        glfwWindowHint(GLFW_RESIZABLE, GLFW_FALSE);

        // create our window
        window = glfwCreateWindow(
                windowWidth, windowHeight,
                windowTitle, NULL, NULL
        );
        if (window == NULL) {
            throw new RuntimeException("Failed to create GLFW window");
        }

        // center the window
        GLFWVidMode vidmode = glfwGetVideoMode(glfwGetPrimaryMonitor());
        glfwSetWindowPos(
                window,
                (vidmode.width() - windowWidth) / 2,
                (vidmode.height() - windowHeight) / 2
        );

        // set our resize callback
        glfwSetWindowSizeCallback(window, new GLFWWindowSizeCallback() {
            @Override
            public void invoke(long l, int width, int height) {
                onResize(width, height);
            }
        });

        // create the capabilities
        glfwMakeContextCurrent(window);
        GL.createCapabilities();

        pixels = MemoryUtil.memAllocInt(resolutionX*resolutionY);

        framebuffer = GL30.glGenFramebuffers();
        GL30.glBindFramebuffer(GL30.GL_FRAMEBUFFER, framebuffer);

        texture = GL11.glGenTextures();
        GL11.glBindTexture(GL11.GL_TEXTURE_2D, texture);

        GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MAG_FILTER, GL11.GL_NEAREST);
        GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER, GL11.GL_NEAREST);
        GL11.glTexImage2D(GL11.GL_TEXTURE_2D, 0, GL11.GL_RGB8, resolutionX, resolutionY, 0, GL12.GL_BGRA, GL11.GL_UNSIGNED_BYTE, 0);

        GL30.glFramebufferTexture2D(GL30.GL_FRAMEBUFFER, GL30.GL_COLOR_ATTACHMENT0, GL11.GL_TEXTURE_2D, texture, 0);
        GL30.glBindFramebuffer(GL30.GL_FRAMEBUFFER, 0);

        onResize(windowWidth, windowHeight);

        graphics = new Graphics(resolutionX, resolutionY, pixels);
    }

    public boolean shouldClose() {
        return glfwWindowShouldClose(window);
    }

    public void swapBuffers() {
        // Clear the screen
        GL11.glClear(GL11.GL_COLOR_BUFFER_BIT);

        // update the buffer
        GL11.glBindTexture(GL11.GL_TEXTURE_2D, texture);
        GL11.glTexSubImage2D(GL11.GL_TEXTURE_2D, 0, 0, 0, resolutionX, resolutionY, GL12.GL_BGRA, GL11.GL_UNSIGNED_BYTE, pixels);

        // blit the buffer
        GL30.glBindFramebuffer(GL30.GL_READ_FRAMEBUFFER, framebuffer);
        GL30.glBindFramebuffer(GL30.GL_DRAW_FRAMEBUFFER, 0);
        GL30.glBlitFramebuffer(
                0, 0,
                resolutionX, resolutionY,
                fPosXS, fPosYS,
                fPosXE, fPosYE,
                GL11.GL_COLOR_BUFFER_BIT,
                GL11.GL_NEAREST
        );

        // actually swap the buffers
        glfwSwapBuffers(window);
    }

    public int width() {
        return windowWidth;
    }

    public int height() {
        return windowHeight;
    }

    public Graphics getGraphics() {
        return graphics;
    }

    private void onResize(int width, int height) {
        this.windowWidth = width;
        this.windowHeight = height;
        GL11.glViewport(0, 0, width, height);
        double xScale = width;
        double yScale = width / aspect;
        if (yScale > height) {
            xScale = height * aspect;
            yScale = height;
        }
        fPosXS = (int)((width - xScale)  * 0.5f);
        fPosYS = (int)((height - yScale) * 0.5f);
        fPosXE = (int)Math.round(xScale) + fPosXS;
        fPosYE = (int)Math.round(yScale) + fPosYS;
    }

}
