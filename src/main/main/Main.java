import alphine.ecs.World;
import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.stage.Stage;
import alphine.logging.Logger;
import alphine.systems.*;

public class Main extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage stage) throws Exception {
        run(stage);
    }

    public void run(Stage stage) {
        Canvas canvas = new Canvas(600, 600);

        GraphicsContext graphics = canvas.getGraphicsContext2D();

        World world = new World();
        world.setDebug(false);

        // Windowing
        world.addSystem(new WindowSystem(stage, canvas));
        world.addSystem(new UiSystem());

        world.addSystem(new TestSystem());

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

        world.start();
        gameLoop.start();

        stage.setOnCloseRequest(e -> {
            gameLoop.stop();
            world.dispose();
        });
    }
}