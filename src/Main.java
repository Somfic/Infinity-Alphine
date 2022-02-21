import components.FlatMaterial;
import components.Shape;
import components.Transform;
import ecs.Entity;
import ecs.worlds.World;
import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.scene.canvas.Canvas;
import javafx.stage.Stage;
import logging.Logger;
import systems.CanvasSystem;
import systems.RenderSystem;

import java.awt.*;

public class Main extends Application {

    private Entity entity;

    public static void main(String[] args) {
        launch(Main.class);
    }

    @Override
    public void start(Stage stage) throws Exception {
        Canvas canvas = new Canvas(800, 600);

        World world = new World();
        world.addSystem(new CanvasSystem(stage, canvas));
        world.addSystem(new RenderSystem(canvas));

        for (int i = 0; i < 100000; i++) {
            entity = world.createEntity("Test");

            // Random position
            entity.addComponent(new Transform().setScale(10, 10).setPosition(Math.random() * 1000 - 500, Math.random() * 800 - 400, 0).setRotation(Math.random() * 360));
            entity.addComponent(new FlatMaterial().setFillColor(Color.getHSBColor((float)Math.random(), (float)Math.random(), (float)Math.random())).setFilled(true));
            entity.addComponent(new Shape(Shape.PrimitiveShape.RECTANGLE));
            //entity.addScript(new TestScript());
        }

        world.start();

        AnimationTimer timer = new AnimationTimer() {
            long last = -1;

            long lastFramerate = -1;
            int frames = 0;

            @Override
            public void handle(long now) {
                if(last == -1)
                    last = now;

                if(lastFramerate == -1)
                    lastFramerate = now;

                double delta = (now - last) / 1e9;

                world.update(delta);
                world.render();

                // FPS counter
                if(now - lastFramerate > 1e9) {
                    Logger.info("FPS: " + frames);
                    frames = 0;
                    lastFramerate = now;
                }

                frames++;
                last = now;
            }
        };

        timer.start();

        stage.setOnCloseRequest(e -> {
            timer.stop();
            world.end();
        });
    }
}

