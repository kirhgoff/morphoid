package org.kirhgoff.morphoid.ascii;

import javafx.geometry.Point2D;
import javafx.scene.paint.Paint;

/**
 * Created by <a href="mailto:kirill.lastovirya@gmail.com">kirhgoff</a> on 12/3/17.
 */
public class AsciiSprite {
  private Point2D origin;
  private final String ascii;
  private final Paint color;

  public AsciiSprite(Point2D origin, String ascii, Paint color) {
    this.origin = origin;
    this.ascii = ascii;
    this.color = color;
  }

  public Point2D getOrigin() {
    return origin;
  }

  public String getAscii() {
    return ascii;
  }

  public Paint getColor() {
    return color;
  }

  @Override
  public String toString() {
    return "AsciiSprite{" +
        "origin=" + origin +
        ", ascii='" + ascii + '\'' +
        ", color=" + color +
        '}';
  }
}
