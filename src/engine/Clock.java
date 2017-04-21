package engine;

/**
 * Created by zeejfps on 4/21/17.
 */
public class Clock {

    public static final double MS_PER_SC = 1000.0;
    public static final double NS_PER_MS = 1000000.0;
    public static final double NS_PER_SC = 1000000000.0;

    private double scale;
    private double prevTime;
    private boolean running;

    private double deltaTimeMS; // delta time in Milliseconds
    private double deltaTimeNS; // delta time in Nanoseconds
    private double deltaTime;   // delta time in Seconds

    public Clock() {
        this(1.0);
    }

    public Clock(double scale) {
        setScale(scale);
    }

    public void start() {
        if (running) return;
        running = true;

        prevTime = System.nanoTime();
    }

    public void stop() {
        running = false;
    }

    public void tick() {
        if (!running) return;
        deltaTimeNS = (System.nanoTime() - prevTime) * scale;
        deltaTimeMS = deltaTimeNS / NS_PER_MS;
        deltaTime = deltaTimeMS / MS_PER_SC;
        prevTime = System.nanoTime();
    }

    public void reset() {
        deltaTimeNS = 0;
        deltaTimeMS = 0;
        prevTime = System.nanoTime();
    }

    public double getDeltaTimeMS() {
        return deltaTimeMS;
    }

    public double getDeltaTimeNS() {
        return deltaTimeNS;
    }

    public double getDeltaTime() {
        return deltaTime;
    }

    public void setScale(double scale) {
        if (scale <= 0.0) scale = 0.1;
        this.scale = scale;
    }

}
