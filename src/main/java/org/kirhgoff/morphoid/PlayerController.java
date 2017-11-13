package org.kirhgoff.morphoid;

import javafx.event.EventHandler;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.locks.Lock;

import static javafx.scene.input.KeyCode.*;

/**
 * Created by <a href="mailto:kirill.lastovirya@gmail.com">kirhgoff</a> on 31/8/17.
 */
public class PlayerController implements EventHandler<KeyEvent> {
  private final PlayerInputState input;
  private Lock inputLock;
  private Set<KeyCode> known_codes = new HashSet<KeyCode>(Arrays.asList(W, S, D, A, LEFT, RIGHT, UP, DOWN));

  // Do I need a lock to sync another lock?
  PlayerController(PlayerInputState input, Lock inputLock) {
    this.input = input;
    this.inputLock = inputLock;
  }

  @Override
  public void handle(KeyEvent event) {
    inputLock.lock();
    try {
      //System.out.println("KeyEvent " + event + " => " + input);

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
      if (!known_codes.contains(event.getCode())) {
        event.consume();
      }
      //System.out.println("PC set " + input);
    } finally {
      inputLock.unlock();
    }
  }
}
