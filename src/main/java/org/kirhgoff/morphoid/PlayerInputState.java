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
  public boolean movingLeft() {return movesLeft.get();}
  public boolean movingRight() {return movesRight.get();}
  public boolean movingUp() {return movesUp.get();}
  public boolean movingDown() {return movesDown.get();}

  public boolean shootingLeft() {return shootsLeft.get();}
  public boolean shootingRight() {return shootsRight.get();}
  public boolean shootingUp() {return shootsUp.get();}
  public boolean shootingDown() {return shootsDown.get();}

  public void receiveMoveLeft() {negate(movesLeft);}
  public void receiveMoveRight() {negate(movesRight);}
  public void receiveMoveUp() {negate(movesUp);}
  public void receiveMoveDown() {negate(movesDown);}

  public void receiveShootLeft() {negate(shootsLeft);}
  public void receiveShootRight() {negate(shootsRight);}
  public void receiveShootUp() {negate(shootsUp);}
  public void receiveShootDown() {negate(shootsDown);}

  // here https://stackoverflow.com/questions/1255617/does-atomicboolean-not-have-a-negate-method
  private void negate(AtomicBoolean flag) {
    boolean v;
    do { v=flag.get(); } while(!flag.compareAndSet(v, !v));
  }

}
