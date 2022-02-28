package systems;

import components.Text;
import components.Transform;
import ecs.Entity;
import ecs.System;
import ecs.World;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;

import java.awt.*;
import java.util.Locale;

public class DebugSystem extends System {

    private final Canvas canvas;
    private final GraphicsContext graphics;
    private Entity fpsCounter;

    public DebugSystem(Canvas canvas) {

        this.canvas = canvas;
        graphics = canvas.getGraphicsContext2D();
    }

    @Override
    public void onStart(World e) {
        this.fpsCounter = new Entity("FPS Counter");
        fpsCounter.addComponent(new Transform().setStatic(true).setPosition(10, 20));
        fpsCounter.addComponent(new Text().setColor(Color.RED));
        getWorld().addEntity(fpsCounter);
    }

    double lastFps = 60;
    double lastDelta = 16;

    @Override
    public void onUpdate(double dt) {
        final double smoothing = 0.99f;

        double averageFps = -1f;
        double averageDelta = -1f;

        if (dt > 0) {
            double fps = 1f / dt;
            double delta = dt * 1000;

            averageFps = (lastFps * smoothing) + fps * (1f - smoothing);
            averageDelta = (lastDelta * smoothing) + delta * (1f - smoothing);

            lastFps = fps;
            lastDelta = delta;

            double health = Math.min((averageFps - 10f) / (60f - 10f), 1f);
            float hue = (float) (health * 0.3f);
            fpsCounter.getComponent(Text.class).setColor(Color.getHSBColor(hue, 1f, 1f)).setText((String.format(Locale.ENGLISH, "%.0f", averageFps) + "fps | " + String.format(Locale.ENGLISH, "%.2f", averageDelta) + "ms"));
        }
    }
}
