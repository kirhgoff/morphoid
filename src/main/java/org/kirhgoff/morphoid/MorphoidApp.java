package org.kirhgoff.morphoid;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import javafx.util.Duration;
import org.kirhgoff.morphoid.ascii.AsciiFactory;
import org.kirhgoff.morphoid.ascii.AsciiSprite;
import org.kirhgoff.morphoid.engine.Entity;
import org.kirhgoff.morphoid.engine.MorphoidEngine;
import org.kirhgoff.morphoid.render.GameGeometry;

import java.io.IOException;
import java.util.List;

/**
 * Created by <a href="mailto:kirill.lastovirya@gmail.com">kirhgoff</a> on 12/3/17.
 */
public class MorphoidApp extends Application {
  //TODO create settings class
  private static final String FONT_NAME = "Monospaced";
  private static final String ASCII_MAP_FILE = "ascii_map.txt";

  private static final int LEVEL_WIDTH = 200;
  private static final int LEVEL_HEIGHT = 100;
  private static final int SCREEN_WIDTH = 640;
  private static final int SCREEN_HEIGHT = 480;
  private static final double FPS_60 = 0.017;

  public static void main(String[] args) throws IOException {
    launch(args);
  }

  @Override
  public void start(Stage stage) {
    GameGeometry geometry = new GameGeometry(SCREEN_WIDTH, SCREEN_HEIGHT, LEVEL_WIDTH, LEVEL_HEIGHT);
    GraphicsContext gc = setupGraphics(stage);
    gc.setFont(Font.font(FONT_NAME, geometry.getFontSize()));

    try {
      MorphoidEngine engine = MorphoidEngine.createSample();
      AsciiFactory ascii = AsciiFactory.makeFor(ASCII_MAP_FILE, SCREEN_WIDTH, SCREEN_HEIGHT);

      Timeline gameLoop = new Timeline();
      gameLoop.setCycleCount(Timeline.INDEFINITE);

      KeyFrame keyFrame = new KeyFrame(
          Duration.seconds(FPS_60),
          actionEvent -> {
            gc.clearRect(0, 0, SCREEN_WIDTH, SCREEN_HEIGHT);

            List<Entity> entities = engine.getEntitiesJava();
            for (Entity entity : entities) {
              AsciiSprite sprite = ascii.getSprite(entity);

              gc.setFill(sprite.getColor());
              gc.fillText(sprite.getAscii(), sprite.getX(), sprite.getY());
            }
          });

      gameLoop.getKeyFrames().add(keyFrame);
      gameLoop.play();

      stage.show();
    } catch (IOException e) {
      //TODO gracefully exit
      e.printStackTrace();
      System.exit(-1);
    }
  }

  private GraphicsContext setupGraphics(Stage stage) {
    stage.setTitle("Morphoid v0.4");
    stage.setFullScreen(true);
    stage.setFullScreenExitHint("");

    Group root = new Group();
    Scene scene = new Scene(root);
    stage.setScene(scene);

    Canvas canvas = new Canvas(640, 480);
    root.getChildren().add(canvas);

    return canvas.getGraphicsContext2D();
  }
}
