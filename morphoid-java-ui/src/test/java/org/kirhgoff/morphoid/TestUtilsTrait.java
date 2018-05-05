package org.kirhgoff.morphoid;

import javafx.beans.property.ReadOnlyDoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;

/**
 * Created by <a href="mailto:kirill.lastovirya@gmail.com">kirhgoff</a> on 2/6/17.
 */
public interface TestUtilsTrait {
  default ReadOnlyDoubleProperty wrap(double value) {
    return new SimpleDoubleProperty(value);
  }
}
