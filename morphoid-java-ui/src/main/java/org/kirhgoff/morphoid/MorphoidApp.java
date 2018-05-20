package org.kirhgoff.morphoid;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.geometry.Point2D;
import javafx.geometry.VPos;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.TextAlignment;
import javafx.stage.Stage;
import javafx.util.Duration;
import org.kirhgoff.morphoid.ascii.AsciiRenderer;
import org.kirhgoff.morphoid.engine.Creature;
//import org.kirhgoff.morphoid.engine.Decoy;
import org.kirhgoff.morphoid.engine.MorphoidEngine;
import org.kirhgoff.morphoid.engine.Physical;
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

  private static String initialScenario = "simple";//"production"; //

  public static void main(String[] args) {
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
    gc.setTextAlign(TextAlignment.CENTER);
    gc.setTextBaseline(VPos.CENTER);

    try {
      AsciiRenderer ascii = AsciiRenderer.makeFor(ASCII_MAP_FILE, geometry);

      Timeline gameLoop = new Timeline();
      gameLoop.setCycleCount(Timeline.INDEFINITE);

      KeyFrame keyFrame = new KeyFrame(
        Duration.seconds(FPS_60),
        actionEvent -> {
          long start = System.nanoTime();
          engineLock.lock();
          try {
            engine.tick();

            cleanScreen(gc, stage);
            //drawLayer(gc, ascii, engine, "decoy");
            drawEntities(gc, ascii, engine);
            drawRate(gc, rate.getAndSet(System.nanoTime() - start));
          } finally {
            engineLock.unlock();
          }
        }
      );

      gameLoop.getKeyFrames().add(keyFrame);
      gameLoop.play();

      stage.setTitle("MorphoidApp v2.0");
      stage.setFullScreenExitHint(""); //TODO add exit button

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

  private void drawRate(GraphicsContext gc, long rate) {
    gc.setStroke(Color.BLACK);
    gc.setFill(Color.BLACK);
    gc.fillText("Framerate: 0." + rate/1000 + " millis", 120, 20);
  }

  private void drawEntities(GraphicsContext gc, AsciiRenderer ascii, MorphoidEngine engine) {
    Collection<Creature> creatures = engine.getCreaturesJava();

    System.out.println("Creatures: " + creatures);

    GameGeometry geometry = ascii.getGeometry();
    double fontSize = geometry.getFontSize();

    Font cellFont = Font.font(FONT_NAME, fontSize);
    Font energyFont = Font.font(FONT_NAME, fontSize / 3);

    double cellWidth = geometry.getCellWidth();
    double cellHeight = geometry.getCellHeight();

    for (Creature decoy : creatures) {

      gc.setFont(cellFont);

      for (Physical cell : decoy.getCellsJava()) {
        drawCell(gc, geometry.convertToScreen(cell), cellWidth, cellHeight,
          ascii.getPalette(engine.creatureType(cell)),
          ascii.getCell(engine.cellType(cell).toString())
        );
      }

      gc.setFont(energyFont);
      drawEnergy(gc, geometry.convertToScreen(decoy.origin()), cellWidth, cellHeight, decoy.getEnergy());
    }
  }

  private void drawEnergy(GraphicsContext gc, Point2D origin, double cellWidth, double cellHeight, double energy) {
    String string = String.format("%4.0f", energy);

    gc.setStroke(Color.YELLOW);
    gc.setFill(Color.YELLOW);

    gc.fillText(string, origin.getX() + cellWidth / 2, origin.getY() + cellHeight / 2);
    gc.strokeText(string, origin.getX() + cellWidth / 2, origin.getY() + cellHeight / 2);
  }

  private void drawCell(GraphicsContext gc, Point2D origin, double cellWidth, double cellHeight, Color color, String chr) {
    gc.setFill(color);
    gc.setStroke(color.darker());

    gc.fillRect(origin.getX(), origin.getY(), cellWidth, cellHeight);
    gc.strokeText(chr, origin.getX() + cellWidth / 2, origin.getY() + cellHeight / 2);
    gc.strokeRect(origin.getX(), origin.getY(), cellWidth, cellHeight);
  }
}
