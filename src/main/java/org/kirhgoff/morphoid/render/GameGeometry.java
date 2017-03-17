package org.kirhgoff.morphoid.render;

/**
 * Created by <a href="mailto:kirill.lastovirya@gmail.com">kirhgoff</a> on 12/3/17.
 */
public class GameGeometry {
  private final int screenWidth;
  private final int screenHeight;
  private final int levelWidth;
  private final int levelHeight;

  private final int cellWidth;
  private final int cellHeight;

  public GameGeometry(int screenWidth, int screenHeight, int levelWidth, int levelHeight) {
    this.screenWidth = screenWidth;
    this.screenHeight = screenHeight;
    this.levelWidth = levelWidth;
    this.levelHeight = levelHeight;

    this.cellWidth = screenWidth/levelWidth;
    this.cellHeight = screenHeight/levelHeight;
    System.out.println(this.toString());
  }

  public double getFontSize() {
    return cellWidth;
  }

  public int convertToScreenX(int x) {
    return cellWidth * x;
  }

  public int convertToScreenY(int y) {
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
