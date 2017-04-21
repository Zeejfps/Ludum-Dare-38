package engine.glfw.callback;

/**
 * Created by Zeejfps on 10/29/2016.
 */
public interface InputCallback {
    void onCursorMove(double x, double y);
    void onKey(int key, int scancode, int action, int mods);
}
