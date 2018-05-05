package org.kirhgoff.morphoid;

import java.util.concurrent.atomic.AtomicBoolean;

/**
 * TODO move to model?
 * Created by <a href="mailto:kirill.lastovirya@gmail.com">kirhgoff</a> on 3/9/17.
 */
public class PlayerInputState {
  private final AtomicBoolean movesLeft = new AtomicBoolean();
  private final AtomicBoolean movesRight = new AtomicBoolean();
  private final AtomicBoolean movesUp = new AtomicBoolean();
  private final AtomicBoolean movesDown = new AtomicBoolean();

  private final AtomicBoolean shootsLeft = new AtomicBoolean();
  private final AtomicBoolean shootsRight = new AtomicBoolean();
  private final AtomicBoolean shootsUp = new AtomicBoolean();
  private final AtomicBoolean shootsDown = new AtomicBoolean();

  // Interface
  public boolean isMovingLeft() {return movesLeft.get();}
  public boolean isMovingRight() {return movesRight.get();}
  public boolean isMovingUp() {return movesUp.get();}
  public boolean isMovingDown() {return movesDown.get();}

  public boolean isShootingLeft() {return shootsLeft.get();}
  public boolean isShootingRight() {return shootsRight.get();}
  public boolean isShootingUp() {return shootsUp.get();}
  public boolean isShootingDown() {return shootsDown.get();}

  //Setters
  public void beginMovingLeft() {movesLeft.set(true);}
  public void beginMovingRight() {movesRight.set(true);}
  public void beginMovingUp() {movesUp.set(true);}
  public void beginMovingDown() {movesDown.set(true);}

  public void beginShootingLeft() {shootsLeft.set(true);}
  public void beginShootingRight() {shootsRight.set(true);}
  public void beginShootingUp() {shootsUp.set(true);}
  public void beginShootingDown() {shootsDown.set(true);}

  public void endMovingLeft() {negate(movesLeft);}
  public void endMovingRight() {negate(movesRight);}
  public void endMovingUp() {negate(movesUp);}
  public void endMovingDown() {negate(movesDown);}

  public void endShootingLeft() {negate(shootsLeft);}
  public void endShootingRight() {negate(shootsRight);}
  public void endShootingUp() {negate(shootsUp);}
  public void endShootingDown() {negate(shootsDown);}

  // here https://stackoverflow.com/questions/1255617/does-atomicboolean-not-have-a-negate-method
  private void negate(AtomicBoolean flag) {
//    boolean v;
//    do { v=flag.get(); } while(!flag.compareAndSet(v, !v));
    flag.set(false);
  }

  @Override
  public String toString() {
    return "PIS{" +
        "mooLeft " + sign(movesLeft) +
        " mooRight " + sign(movesRight) +
        " mooUp " + sign(movesUp) +
        " mooDown " + sign(movesDown) +
        " shooLeft " + sign(shootsLeft) +
        " shooRight " + sign(shootsRight) +
        " shooUp " + sign(shootsUp) +
        " shooDown " + sign(shootsDown) +
        '}';
  }

  private String sign(AtomicBoolean value) {
    return value.get() ? "+" : "-";
  }
}
