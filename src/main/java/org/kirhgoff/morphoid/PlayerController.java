package org.kirhgoff.morphoid;

import javafx.event.EventHandler;
import javafx.scene.input.KeyEvent;

import java.util.concurrent.locks.Lock;

/**
 * Created by <a href="mailto:kirill.lastovirya@gmail.com">kirhgoff</a> on 31/8/17.
 */
public class PlayerController implements EventHandler<KeyEvent> {
  private final PlayerInputState input;
  private Lock inputLock;

  // Do I need a lock to sync another lock?
  PlayerController(PlayerInputState input, Lock inputLock) {
    this.input = input;
    this.inputLock = inputLock;
  }

  @Override
  public void handle(KeyEvent event) {
    inputLock.lock();
    try {
      System.out.println("KeyEvent " + event + " => " + input);

      if (event.getEventType().equals(KeyEvent.KEY_PRESSED)) {
        //System.out.println("KeyPressed " + event.getCode());
        switch (event.getCode()) {
        case W: input.beginMovingUp();
          break;
        case S: input.beginMovingDown();
          break;
        case A: input.beginMovingLeft();
          break;
        case D: input.beginMovingRight();
          break;
        case LEFT: input.beginShootingLeft();
          break;
        case RIGHT: input.beginShootingRight();
          break;
        case UP: input.beginShootingUp();
          break;
        case DOWN: input.beginShootingDown();
          break;
        }
      } else if (event.getEventType().equals(KeyEvent.KEY_RELEASED)) {
        //System.out.println("KeyReleased " + event.getCode());
        switch (event.getCode()) {
        case W: input.endMovingUp();
          break;
        case S: input.endMovingDown();
          break;
        case A: input.endMovingLeft();
          break;
        case D: input.endMovingRight();
          break;
        case LEFT: input.endShootingLeft();
          break;
        case RIGHT: input.endShootingRight();
          break;
        case UP: input.endShootingUp();
          break;
        case DOWN: input.endShootingDown();
          break;
        }
      }
      event.consume(); //TODO consume only when ours
      //System.out.println("PC set " + input);
    } finally {
      inputLock.unlock();
    }
  }
}
