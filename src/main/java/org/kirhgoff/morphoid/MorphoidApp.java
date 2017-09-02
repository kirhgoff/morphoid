package org.kirhgoff.morphoid;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.geometry.Point2D;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.KeyEvent;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import javafx.util.Duration;
import org.kirhgoff.morphoid.ascii.AsciiFactory;
import org.kirhgoff.morphoid.ascii.AsciiSprite;
import org.kirhgoff.morphoid.engine.Creature;
import org.kirhgoff.morphoid.engine.MorphoidEngine;
import org.kirhgoff.morphoid.render.GameGeometry;

import java.io.IOException;
import java.util.Collection;

/**
  * Created by <a href="mailto:kirill.lastovirya@gmail.com">kirhgoff</a> on 12/3/17.
  */
public class MorphoidApp extends Application {
  //TODO create settings class
  private static final String FONT_NAME = "Monospaced";
  private static final String ASCII_MAP_FILE = "sample_map.txt";

  private static final int LEVEL_WIDTH = 20;
  private static final int LEVEL_HEIGHT = 20;
  private static final int SCREEN_WIDTH = 640;
  private static final int SCREEN_HEIGHT = 480;
  private static final double FPS_60 = 0.017;

  public static void main(String[] args) throws IOException {
    launch(args);
  }

  @Override
  public void start(Stage stage) {
    // Model
    MorphoidEngine engine = MorphoidEngine.createSample();
    Group root = new Group();
    Scene scene = new Scene(root, SCREEN_WIDTH, SCREEN_HEIGHT);
    scene.setOnKeyPressed(new KeyEventHandler(engine));
    stage.setScene(scene);

    Canvas canvas = new Canvas(SCREEN_WIDTH, SCREEN_HEIGHT);
    root.getChildren().add(canvas);

    //TODO what is this?
    canvas.widthProperty().bind(stage.widthProperty());
    canvas.heightProperty().bind(stage.heightProperty());

    GameGeometry geometry = new GameGeometry(
        stage.widthProperty(), stage.heightProperty(),
        LEVEL_WIDTH, LEVEL_HEIGHT);

    GraphicsContext gc = canvas.getGraphicsContext2D();
    gc.setFont(Font.font(FONT_NAME, geometry.getFontSize()));

    try {
      AsciiFactory ascii = AsciiFactory.makeFor(ASCII_MAP_FILE, geometry);

      Timeline gameLoop = new Timeline();
      gameLoop.setCycleCount(Timeline.INDEFINITE);

      KeyFrame keyFrame = new KeyFrame(
          Duration.seconds(FPS_60),
          actionEvent -> {

            engine.tick();

            cleanScreen(gc, stage);

            //TODO synchronize
            drawEntities(gc, ascii, engine.getEntitiesJava());
          }
      );

      gameLoop.getKeyFrames().add(keyFrame);
      gameLoop.play();

      stage.setTitle("Morphoid v0.4");
      stage.setFullScreenExitHint("");

      stage.show();
      stage.setFullScreen(true);

    } catch (IOException e) {
      //TODO gracefully exit
      e.printStackTrace();
      System.exit(-1);
    }
  }

  private void cleanScreen(GraphicsContext gc, Stage stage) {
    gc.clearRect(0, 0, stage.getWidth(), stage.getHeight());
  }

  private void drawEntities(GraphicsContext gc, AsciiFactory ascii, Collection<Creature> entities) {
    for (Creature entity : entities) {
      AsciiSprite sprite = ascii.getSprite(entity);
      Point2D origin = sprite.getOrigin();

      gc.setStroke(sprite.getColor());
      gc.setFill(sprite.getColor());
      gc.fillText(sprite.getAscii(), origin.getX(), origin.getY());
      gc.strokeText(sprite.getAscii(), origin.getX(), origin.getY());
    }
  }

}
