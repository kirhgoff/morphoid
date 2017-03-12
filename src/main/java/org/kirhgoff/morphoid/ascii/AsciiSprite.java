package org.kirhgoff.morphoid.ascii;

import javafx.scene.paint.Paint;

/**
 * Created by <a href="mailto:kirill.lastovirya@gmail.com">kirhgoff</a> on 12/3/17.
 */
public class AsciiSprite {
  private int x;
  private int y;
  private final String ascii;
  private final Paint color;

  public AsciiSprite(int x, int y, String ascii, Paint color) {
    this.x = x;
    this.y = y;
    this.ascii = ascii;
    this.color = color;
  }

  public int getX() {
    return x;
  }

  public int getY() {
    return y;
  }

  public String getAscii() {
    return ascii;
  }

  public Paint getColor() {
    return color;
  }

}
