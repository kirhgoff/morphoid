package org.kirhgoff.morphoid.render;

import javafx.beans.property.ReadOnlyDoubleProperty;
import javafx.geometry.Point2D;
import org.kirhgoff.morphoid.engine.Physical;

/**
  * Created by <a href="mailto:kirill.lastovirya@gmail.com">kirhgoff</a> on 12/3/17.
  */
public class GameGeometry {
  private final ReadOnlyDoubleProperty screenWidth;
  private final ReadOnlyDoubleProperty screenHeight;
  private final int levelWidth;
  private final int levelHeight;

  private double cellWidth;
  private double cellHeight;

  public GameGeometry(ReadOnlyDoubleProperty screenWidth, ReadOnlyDoubleProperty screenHeight, int levelWidth, int levelHeight) {
    this.screenWidth = screenWidth;
    this.screenHeight = screenHeight;
    this.levelWidth = levelWidth;
    this.levelHeight = levelHeight;

    updateRatio();
    screenWidth.addListener(event -> updateRatio());
    screenHeight.addListener(event -> updateRatio());
  }

  // TODO reimplement with atomics

  private synchronized void updateRatio() {
    double width = screenWidth.doubleValue();
    double height = screenHeight.doubleValue();

    //Some default values
    if (Double.isNaN(width)) width = 640;
    if (Double.isNaN(height)) height = 480;

    this.cellWidth = width/levelWidth;
    this.cellHeight = height/levelHeight;

    //System.out.println("Updated: " + toString());
  }

  public synchronized double getFontSize() {
    return cellWidth;
  }

  public synchronized double getCellWidth() {
    return cellWidth;
  }

  public synchronized double getCellHeight() {
    return cellHeight;
  }

  public synchronized Point2D convertToScreen(int x, int y) {
    return new Point2D(cellWidth * x, cellHeight * y);
  }

  public synchronized Point2D convertToScreen(Physical origin) {
    return convertToScreen(origin.x(), origin.y());
  }

  @Override
  public String toString() {
    return "GameGeometry{" +
        " levelWidth=" + levelWidth +
        ", levelHeight=" + levelHeight +
        ", cellWidth=" + cellWidth +
        ", cellHeight=" + cellHeight +
        '}';
  }
}
