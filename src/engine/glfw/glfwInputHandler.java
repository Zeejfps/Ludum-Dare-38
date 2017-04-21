package engine.glfw;

import engine.Window;
import engine.glfw.callback.InputCallback;

import java.util.Arrays;

import static org.lwjgl.glfw.GLFW.*;

/**
 * Created by Zeejfps on 6/15/2016.
 */
public class glfwInputHandler implements InputCallback {

    private static final int MAX_KEYS = 600;

    private boolean[] keysDown = new boolean[MAX_KEYS];
    private boolean[] keysPressed = new boolean[MAX_KEYS];
    private boolean[] keysReleased = new boolean[MAX_KEYS];

    public double mouseX, mouseY;

    public glfwInputHandler(Window window) {
        window.addInputListener(this);
    }

    public boolean isKeyDown(int keycode) {
        return keysDown[keycode];
    }

    public boolean wasKeyPressed(int keycode) {
        return keysPressed[keycode];
    }

    public boolean wasKeyReleased(int keycode) {
        return keysReleased[keycode];
    }

    public void update() {
        Arrays.fill(keysPressed, false);
        Arrays.fill(keysReleased, false);
    }

    @Override
    public void onCursorMove(double x, double y) {
        mouseX = (float)x;
        mouseY = (float)y;
    }

    @Override
    public void onKey(int key, int scancode, int action, int mods) {
        switch (action) {
            case GLFW_REPEAT:
                break;
            case GLFW_PRESS:
                keysDown[key] = true;
                keysPressed[key] = true;
                keysReleased[key] = false;
                break;
            case GLFW_RELEASE:
                keysDown[key] = false;
                keysPressed[key] = false;
                keysReleased[key] = true;
                break;
        }
    }

}
