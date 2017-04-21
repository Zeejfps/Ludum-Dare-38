package engine;

import engine.glfw.callback.SizeCallback;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL30;

/**
 * Created by zeejfps on 4/21/17.
 */
public class Canvas {

    private final Window window;
    private final Framebuffer framebuffer;

    public Canvas(Window window, int width, int height) {
        this.window = window;
        this.framebuffer = new Framebuffer(width, height);

        this.window.addSizeCallback(new SizeCallback() {
            @Override
            public void onResize(int width, int height) {
                int width = window.width();
                int height = window.height();
                double aspect = fb.width() / (float)fb.height();
                double xScale= width;
                double yScale= width / aspect;
                if (yScale > height) {
                    xScale = height * aspect;
                    yScale = height;
                }
                fPosXS = (int)((width - xScale)  * 0.5f);
                fPosYS = (int)((height - yScale) * 0.5f);
                fPosXE = (int)Math.round(xScale) + fPosXS;
                fPosYE = (int)Math.round(yScale) + fPosYS;
                needResize = false;
            }
        });
    }

    public void render() {
        GL11.glClear(GL11.GL_COLOR_BUFFER_BIT);
        fb.update();
        GL30.glBindFramebuffer(GL30.GL_READ_FRAMEBUFFER, fb.id());
        GL30.glBindFramebuffer(GL30.GL_DRAW_FRAMEBUFFER, 0);
        GL30.glBlitFramebuffer(
                0, 0,
                fb.width(), fb.height(),
                fPosXS, fPosYS,
                fPosXE, fPosYE,
                GL11.GL_COLOR_BUFFER_BIT,
                GL11.GL_NEAREST
        );

        window.swapBuffers();
    }

}
