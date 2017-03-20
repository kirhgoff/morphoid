package org.kirhgoff.morphoid.render;

/**
 * Created by <a href="mailto:kirill.lastovirya@gmail.com">kirhgoff</a> on 12/3/17.
 */
public class GameGeometry {
  private final double screenWidth;
  private final double screenHeight;
  private final int levelWidth;
  private final int levelHeight;

  private final double cellWidth;
  private final double cellHeight;

  public GameGeometry(double screenWidth, double screenHeight, int levelWidth, int levelHeight) {
    this.screenWidth = screenWidth;
    this.screenHeight = screenHeight;
    this.levelWidth = levelWidth;
    this.levelHeight = levelHeight;

    this.cellWidth = screenWidth/levelWidth;
    this.cellHeight = screenHeight/levelHeight;
  }

  public double getFontSize() {
    return cellWidth;
  }

  public double convertToScreenX(int x) {
    return cellWidth * x;
  }

  public double convertToScreenY(int y) {
    return cellHeight * y;
  }

  @Override
  public String toString() {
    return "GameGeometry{" +
        "screenWidth=" + screenWidth +
        ", screenHeight=" + screenHeight +
        ", levelWidth=" + levelWidth +
        ", levelHeight=" + levelHeight +
        ", cellWidth=" + cellWidth +
        ", cellHeight=" + cellHeight +
        '}';
  }
}
