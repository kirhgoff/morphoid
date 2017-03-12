package org.kirhgoff.morphoid.render;

/**
 * Created by <a href="mailto:kirill.lastovirya@gmail.com">kirhgoff</a> on 12/3/17.
 */
public class GameGeometry {
  private final int screenWidth;
  private final int screenHeight;
  private final int levelWidth;
  private final int levelHeight;

  public GameGeometry(int screenWidth, int screenHeight, int levelWidth, int levelHeight) {
    this.screenWidth = screenWidth;
    this.screenHeight = screenHeight;
    this.levelWidth = levelWidth;
    this.levelHeight = levelHeight;
  }

  public double getFontSize() {
    return 20;
  }

  public int convertToScreenX(int x) {
    return 0; //TODO
  }

  public int convertToScreenY(int y) {
    return 0; //TODO
  }

}
