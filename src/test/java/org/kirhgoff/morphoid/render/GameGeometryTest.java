package org.kirhgoff.morphoid.render;

import javafx.geometry.Point2D;
import org.kirhgoff.morphoid.TestUtilsTrait;
import org.testng.annotations.Test;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Created by <a href="mailto:kirill.lastovirya@gmail.com">kirhgoff</a> on 13/3/17.
 */
public class GameGeometryTest implements TestUtilsTrait {

  @Test
  public void testConversion() throws Exception {
    GameGeometry geometry =
        new GameGeometry(wrap(100), wrap(200), 10, 10);

    checkLevelToScreen(geometry, 0, 0, 0, 0);
    checkLevelToScreen(geometry, 9, 9, 100 - 10, 200 - 20);
    checkLevelToScreen(geometry, -1, 20, - 10, 400);

  }

  private void checkLevelToScreen(GameGeometry geometry, int x, int y, int expectedX, int expectedY) {
    Point2D point = geometry.convertToScreen(x, y);
    assertThat(point.getX()).isEqualTo(expectedX);
    assertThat(point.getY()).isEqualTo(expectedY);
  }
}