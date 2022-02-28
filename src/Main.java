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
import javafx.scene.canvas.GraphicsContext;
import javafx.stage.Stage;
import logging.Logger;
import org.jfree.fx.FXGraphics2D;
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

        // Windowing
        world.addSystem(new CanvasSystem(stage, scene, canvas));

        // Debug
        //world.addSystem(new TextSystem(canvas));
        world.addSystem(new DebugSystem(canvas));

        // Input
        world.addSystem(new InputSystem(scene));

        // Other
        world.addSystem(new ObjectTest());

        // Physics
        world.addSystem(new MotionSystem());

        // Rendering
        world.addSystem(new RenderSystem(canvas));

        AnimationTimer timer = new AnimationTimer() {
            long last = -1;

            double fixedTimeStep = 1.0 / 60.0;
            double accumulator = 0.0;
            double alpha;

            @Override
            public void handle(long now) {
                if (last == -1)
                    last = now;

                double dt = (now - last) / 1e9;
                last = now;

                // Fixed Update
                accumulator += dt;
                while(accumulator >= fixedTimeStep) {
                    world.fixedUpdate(fixedTimeStep);
                    accumulator -= fixedTimeStep;
                }
                //alpha = accumulator / fixedTimeStep;

                // Update
                world.update(dt);

                int width = (int)Math.ceil(canvas.getWidth());
                int height = (int)Math.ceil(canvas.getHeight());

                // Render
                world.render();
            }
        };

        timer.start();

        stage.setOnCloseRequest(e -> {
            timer.stop();
            world.dispose();
        });
    }
}

