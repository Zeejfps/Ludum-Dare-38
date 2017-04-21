package engine;

/**
 * Created by zeejfps on 4/21/17.
 */
public class Loop implements Runnable {

    private static final double NS_PER_SECOND = 1000000000.0;
    private static final int MAX_FRAMES_SKIP = 5;

    private final Listener listener;
    private volatile boolean running;

    private double nsPerUpdate;

    public Loop(final Listener listener) {
        this(listener, 30);
    }

    public Loop(final Listener listener, double fixedUpdateInterval) {
        this.listener = listener;
        setFixedUpdateInterval(fixedUpdateInterval);
    }

    @Override
    public void run() {

        if (running) return;
        running = true;

        int skippedFrames = 0;

        double lag = 0, current, elapsed;
        double previous = System.nanoTime();

        listener.onStart();
        while(running) {
            current = System.nanoTime();
            elapsed = current - previous;

            previous = current;
            lag += elapsed;

            while (lag >= nsPerUpdate && skippedFrames < MAX_FRAMES_SKIP) {
                listener.onFixedUpdate();
                lag -= nsPerUpdate;
                skippedFrames++;
            }
            skippedFrames = 0;

            listener.onUpdate();
            listener.onRender();
        }
        listener.onStop();
    }

    public void stop() {
        running = false;
    }

    public void setFixedUpdateInterval(double interval) {
        if (interval < 1.0) interval = 1.0;
        nsPerUpdate = NS_PER_SECOND / interval;
    }

    public interface Listener {
        void onStart();
        void onFixedUpdate();
        void onUpdate();
        void onRender();
        void onStop();
    }

}
