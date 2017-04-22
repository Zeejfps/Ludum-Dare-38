package ld38;

import engine.*;
import org.lwjgl.glfw.GLFWErrorCallback;

import static org.lwjgl.glfw.GLFW.*;

/**
 * Created by zeejfps on 4/21/17.
 */
public class Game implements Loop.Listener {

    private final Loop gameLoop;
    private final Clock gameClock;

    private final Display display;

    public Game() {
        GLFWErrorCallback.createPrint(System.err).set();

        if (!glfwInit()) {
            throw new IllegalStateException("Failed to initialize GLFW");
        }

        display = new Display(640, 480, 320, 240, "Game");

        gameLoop = new Loop(this);
        gameClock = new Clock();
    }

    public void launch() {
        gameLoop.run();
    }

    @Override
    public void onStart() {
        gameClock.start();
    }

    @Override
    public void onFixedUpdate() {
        gameClock.tick();
    }

    @Override
    public void onUpdate() {
        glfwPollEvents();
        if (display.shouldClose()) {
            gameLoop.stop();
        }
    }

    int fps = 0;
    long startTime = System.currentTimeMillis();
    @Override
    public void onRender() {
        Graphics g = display.getGraphics();
        g.fillRect(0, 0, g.width(), g.height(), 0xffff00ff);
        display.swapBuffers();
        fps++;
        if (System.currentTimeMillis() - startTime >= 1000) {
            System.out.println("FPS: " + fps);
            fps = 0;
            startTime = System.currentTimeMillis();
        }
    }

    @Override
    public void onStop() {
        glfwTerminate();
    }

    public static void main(String[] args) {
        new Game().launch();
    }

}
