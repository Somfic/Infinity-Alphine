import components.FlatMaterial;
import components.Shape;
import components.Text;
import components.Transform;
import ecs.Entity;
import ecs.World;
import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.stage.Stage;
import systems.*;

import java.awt.*;
import java.util.Locale;

public class Main extends Application {

    private Entity fpsCounter;

    public static void main(String[] args) {
        launch(Main.class);
    }

    @Override
    public void start(Stage stage) throws Exception {
        Canvas canvas = new Canvas(800, 600);
        Scene scene = new Scene(new Group(canvas));

        World world = new World();
        world.setDebug(true);

        world.addSystem(new CanvasSystem(stage, scene, canvas));
        world.addSystem(new RenderSystem(canvas));
        world.addSystem(new TextSystem(canvas));
        world.addSystem(new InputSystem(scene));
        world.addSystem(new MotionSystem());

        world.addSystem(new MotionTestSystem());

        this.fpsCounter = new Entity("FPS Counter");
        fpsCounter.addComponent(new Transform().setStatic(true).setPosition(10, 20));
        fpsCounter.addComponent(new Text().setColor(Color.RED));
        world.addEntity(fpsCounter);

        AnimationTimer timer = new AnimationTimer() {
            long last = -1;

            final double smoothing = 0.99f;

            double averageFps = -1f;
            double averageDelta = -1f;

            @Override
            public void handle(long now) {
                if (last == -1)
                    last = now;

                double deltaSeconds = (now - last) / 1e9;
                double deltaMillis = (now - last) / 1e6;

                world.update(deltaSeconds);

                long delta = now - last;
                double fps = 1f / delta * 1e9;

                if (averageFps < 0) {
                    averageFps = 60;
                } else {
                    averageFps = averageFps * smoothing + fps * (1.0 - smoothing);
                }

                if (averageDelta < 0) {
                    averageDelta = 16.666666;
                } else {
                    averageDelta = averageDelta * smoothing + deltaMillis * (1.0 - smoothing);
                }

                double health = Math.min((averageFps - 10f) / (60f - 10f), 1f);
                float hue = (float) (health * 0.3f);
                fpsCounter.getComponent(Text.class).setColor(Color.getHSBColor(hue, 1f, 1f));
                fpsCounter.getComponent(Text.class).setText(String.format(Locale.ENGLISH, "%.0f", averageFps) + "fps | " + String.format(Locale.ENGLISH, "%.2f", averageDelta) + "ms");

                last = now;
            }
        };

        timer.start();

        stage.setOnCloseRequest(e -> {
            timer.stop();
            world.dispose();
        });
    }
}

