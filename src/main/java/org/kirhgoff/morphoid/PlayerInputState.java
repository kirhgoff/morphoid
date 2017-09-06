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

}
