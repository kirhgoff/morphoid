package org.kirhgoff.morphoid;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.geometry.Point2D;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
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
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
  * Created by <a href="mailto:kirill.lastovirya@gmail.com">kirhgoff</a> on 12/3/17.
  */
public class MorphoidApp extends Application {
  //TODO create settings class
  private static final String FONT_NAME = "Monospaced";
  private static final String ASCII_MAP_FILE = "sample_map.txt";

  private static final int SCREEN_WIDTH = 640;
  private static final int SCREEN_HEIGHT = 480;
  private static final double FPS_60 = 0.017;
  private static final int LEVEL_HEIGHT = 30;
  private static final int LEVEL_WIDTH = 30;

  private static String initialScenario = "production";

  public static void main(String[] args) throws IOException {
    if (args.length == 1) initialScenario = args[0];
    launch(args);
  }

  private Lock inputLock = new ReentrantLock();
  private Lock engineLock = new ReentrantLock();

  @Override
  public void start(Stage stage) {
    AtomicLong rate = new AtomicLong();
    // Model
    MorphoidEngine engine;
    PlayerController playerController;
    PlayerInputState playerInputState = new PlayerInputState();

    Group root = new Group();
    Scene scene = new Scene(root, SCREEN_WIDTH, SCREEN_HEIGHT);

    inputLock.lock();
    try {
      engine = MorphoidEngine.create(initialScenario, playerInputState);
      playerController = new PlayerController(playerInputState, inputLock);
      // TODO split in two sync blocks

      scene.setOnKeyPressed(playerController);
      scene.setOnKeyReleased(playerController);

    } finally {
      inputLock.unlock();
    }

    stage.setScene(scene);

    Canvas canvas = new Canvas(SCREEN_WIDTH, SCREEN_HEIGHT);
    root.getChildren().add(canvas);

    //TODO what is this? I knew that
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
            long start = System.currentTimeMillis();
            engineLock.lock();
            try {
              engine.tick();

              cleanScreen(gc, stage);

              //TODO synchronize
              drawEntities(gc, ascii, engine.getEntitiesJava());
              drawRate(gc, rate.getAndSet(System.currentTimeMillis() - start));
            } finally {
              engineLock.unlock();
            }
          }
      );

      gameLoop.getKeyFrames().add(keyFrame);
      gameLoop.play();

      stage.setTitle("MorphoidApp v0.3");
      stage.setFullScreenExitHint(""); //TODO add exit button

      stage.show();
      //stage.setFullScreen(true);

    } catch (IOException e) {
      //TODO gracefully exit
      e.printStackTrace();
      System.exit(-1);
    }
  }

  private void drawRate(GraphicsContext gc, long rate) {
    gc.fillText("Framerate: " + rate + " ms", 20, 20);
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
