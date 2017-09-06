package org.kirhgoff.morphoid;

import javafx.event.EventHandler;
import javafx.scene.input.KeyEvent;
import org.kirhgoff.morphoid.engine.*;

/**
 * Created by <a href="mailto:kirill.lastovirya@gmail.com">kirhgoff</a> on 31/8/17.
 */
public class PlayerController implements EventHandler<KeyEvent> {
  private final PlayerInputState user;
  private final MorphoidEngine engine; //TODO synchronize

  public PlayerController(MorphoidEngine engine, PlayerInputState user) {
    this.engine = engine;
    this.user = user;
  }

  @Override
  public void handle(KeyEvent event) {
    if (event.getEventType().equals(KeyEvent.KEY_PRESSED)) {
      switch (event.getCode()) {
        case W: engine.playerMoves(North$.MODULE$); break;
        case S: engine.playerMoves(South$.MODULE$); break;
        case A: engine.playerMoves(West$.MODULE$); break;
        case D: engine.playerMoves(East$.MODULE$); break;
        case LEFT: engine.playerShoot(West$.MODULE$); break;
        case RIGHT: engine.playerShoot(East$.MODULE$); break;
        case UP: engine.playerShoot(North$.MODULE$); break;
        case DOWN: engine.playerShoot(South$.MODULE$); break;
      }
    } else if (event.getEventType().equals(KeyEvent.KEY_RELEASED)) {
      //TODO
    }
    event.consume();
  }
}
