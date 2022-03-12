package game;

import alphine.components.CameraComponent;
import alphine.components.TransformComponent;
import alphine.ecs.Entity;
import alphine.ecs.World;
import game.terrain.TerrainSystem;
import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.stage.Stage;
import alphine.logging.Logger;
import alphine.systems.*;

public class Magma extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    private void onStart(World world) {
        Entity camera = new Entity("Camera");
        camera.addComponent(new TransformComponent());
        camera.addComponent(new CameraComponent().setZoom(20));
        world.addEntity(camera);
    }

    @Override
    public void start(Stage stage) {
        run(stage);
    }

    public void run(Stage stage) {
        Canvas canvas = new Canvas(600, 600);
        Scene scene = new Scene(new Group(canvas));

        GraphicsContext graphics = canvas.getGraphicsContext2D();

        World world = new World();
        world.setDebug(false);

        onStart(world);

        world.addSystem(new TerrainSystem(scene));
        world.addSystem(new WindowSystem(stage, scene, canvas));
        world.addSystem(new IsometricRenderSystem(canvas));
        world.addSystem(new UiSystem());
        world.addSystem(new FpsSystem());
        world.addSystem(new CameraSystem(scene));

        world.start();

        AnimationTimer gameLoop = new AnimationTimer() {
            long last = -1;

            @Override
            public void start() {
                Logger.debug("Starting game loop");
                super.start();
            }

            @Override
            public void handle(long now) {
                if (last == -1)
                    last = now;

                double dt = (now - last) / 1e9;
                last = now;

                world.fixedUpdate(dt, 1f / 60f);
                world.update(dt);
                world.render(graphics);
            }
        };
        gameLoop.start();

        stage.setOnCloseRequest(e -> {
            gameLoop.stop();
            world.dispose();
        });
    }
}