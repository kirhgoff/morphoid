package org.kirhgoff.morphoid;

import javafx.event.EventHandler;
import javafx.scene.input.KeyEvent;
import org.kirhgoff.morphoid.engine.MorphoidEngine;

/**
 * Created by <a href="mailto:kirill.lastovirya@gmail.com">kirhgoff</a> on 31/8/17.
 */
public class KeyEventHandler implements EventHandler<KeyEvent> {
  private MorphoidEngine engine; //TODO synchronize

  public KeyEventHandler(MorphoidEngine engine) {
    this.engine = engine;
  }

  @Override
  public void handle(KeyEvent event) {
    switch (event.getCode()) {
      case W: engine.playerMoveNorth(); break;
      case S: engine.playerMoveSouth(); break;
      case A: engine.playerMoveWest(); break;
      case D: engine.playerMoveEast(); break;
      case LEFT: engine.playerShootLeft(); break;
      case RIGHT: engine.playerShootRight(); break;
      case UP: engine.playerShootUp(); break;
      case DOWN: engine.playerShootDown(); break;
    }
    event.consume();
  }
}
