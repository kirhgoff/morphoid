package org.kirhgoff.morphoid;

import javafx.event.EventHandler;
import javafx.scene.input.KeyEvent;
import org.kirhgoff.morphoid.engine.*;

/**
 * Created by <a href="mailto:kirill.lastovirya@gmail.com">kirhgoff</a> on 31/8/17.
 */
public class PlayerController implements EventHandler<KeyEvent> {
  private final UserInputState user;
  private final MorphoidEngine engine; //TODO synchronize

  public PlayerController(MorphoidEngine engine, UserInputState user) {
    this.engine = engine;
    this.user = user;
  }

  @Override
  public void handle(KeyEvent event) {
    if (event.getEventType().equals(KeyEvent.KEY_PRESSED))
      switch (event.getCode()) {
        case W: engine.playerStartMoving(North$.MODULE$); event.consume(); break;
        case S: engine.playerStartMoving(South$.MODULE$); event.consume(); break;
        case A: engine.playerStartMoving(West$.MODULE$); event.consume(); break;
        case D: engine.playerStartMoving(East$.MODULE$); event.consume(); break;
        case LEFT: engine.playerShoot(West$.MODULE$); event.consume(); break;
        case RIGHT: engine.playerShoot(East$.MODULE$); event.consume(); break;
        case UP: engine.playerShoot(North$.MODULE$); event.consume(); break;
        case DOWN: engine.playerShoot(South$.MODULE$); event.consume(); break;
      }

  }

  //:)
  private Direction$ m(Direction$ d) {
    return d.MODULE$;
  }
}
