package org.kirhgoff.morphoid;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import javafx.util.Duration;

/**
 * Created by <a href="mailto:kirill.lastovirya@gmail.com">kirhgoff</a> on 12/3/17.
 */
public class Main extends Application {
  public static void main(String[] args) {
    launch(args);
  }

  @Override
  public void start(Stage theStage) {
    theStage.setTitle("Timeline Example");

    Group root = new Group();
    Scene theScene = new Scene(root);
    theStage.setScene(theScene);

    Canvas canvas = new Canvas(512, 512);
    root.getChildren().add(canvas);

    GraphicsContext gc = canvas.getGraphicsContext2D();

    Image earth = new Image("earth.png");
    Image sun = new Image("sun.png");
    Image space = new Image("space.png");

    Timeline gameLoop = new Timeline();
    gameLoop.setCycleCount(Timeline.INDEFINITE);

    final long timeStart = System.currentTimeMillis();

    KeyFrame kf = new KeyFrame(
        Duration.seconds(0.017),                // 60 FPS
        actionEvent -> {
          double t = (System.currentTimeMillis() - timeStart) / 1000.0;

          double x = 232 + 128 * Math.cos(t);
          double y = 232 + 128 * Math.sin(t);

          // Clear the canvas
          gc.clearRect(0, 0, 512, 512);

          // background image clears canvas
          gc.drawImage(space, 0, 0);
          gc.drawImage(earth, x, y);
          gc.drawImage(sun, 196, 196);
        });

    gameLoop.getKeyFrames().add(kf);
    gameLoop.play();

    theStage.show();
  }
}
