package org.kirhgoff.morphoid.engine

import org.scalatest._

class EntityTest extends FlatSpec with Matchers {

  "Entity" should "be able to calculate its origin point" in {
    new Entity("", "", List(Cell(0, 0), Cell(2, 5))).origin should be(Cell(0, 5))
  }
}