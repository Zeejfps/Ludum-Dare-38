package ld38;

import engine.Canvas;
import engine.Clock;
import engine.Loop;
import engine.glfw.callback.SizeCallback;
import engine.glfw.glfwInputHandler;
import engine.Window;
import org.lwjgl.glfw.GLFWErrorCallback;
import org.lwjgl.opengl.GL11;

import static org.lwjgl.glfw.GLFW.*;

/**
 * Created by zeejfps on 4/21/17.
 */
public class Game implements Loop.Listener, SizeCallback {

    private final Loop gameLoop;
    private final Clock gameClock;

    private final Window window;
    private final Canvas canvas;

    public Game() {
        GLFWErrorCallback.createPrint(System.err).set();

        if (!glfwInit()) {
            throw new IllegalStateException("Failed to initialize GLFW");
        }

        window = new Window(640, 480, "Game");
        canvas = new Canvas(window, 320, 240);

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
        if (window.shouldClose()) {
            gameLoop.stop();
        }
    }

    @Override
    public void onRender() {
        GL11.glClear(GL11.GL_COLOR_BUFFER_BIT);
        //System.out.println("Render");
        window.swapBuffers();
    }

    @Override
    public void onStop() {
        window.free();
        glfwTerminate();
    }

    public static void main(String[] args) {
        new Game().launch();
    }

    @Override
    public void onResize(int width, int height) {
        GL11.glViewport(0, 0, width, height);
    }
}
