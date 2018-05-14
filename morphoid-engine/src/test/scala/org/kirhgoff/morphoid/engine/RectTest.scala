package org.kirhgoff.morphoid.engine

import org.scalamock.scalatest.MockFactory
import org.scalatest.{FlatSpec, Matchers}

// TODO convert to FunSpec: http://doc.scalatest.org/3.0.1/#org.scalatest.FunSpec
class RectTest extends FlatSpec with Matchers with MockFactory {

  // TODO change Rect to method name
  "Rect" should "be possible to check if it is inside" in {
    Rect(0, 0, 10, 10).includes(Rect(1,1, 2,2)) shouldBe true
    Rect(0, 0, 10, 10).includes(Rect(1,1, 12,2)) shouldBe false
    Rect(0, 0, 10, 10).includes(Rect(-10,-10, -2, -2)) shouldBe false
  }

  "Rect" should "be able to detect intersects" in {
    // include examples
    Rect(0, 0, 10, 10).intersects(Rect(1,1, 2,2)) shouldBe true
    Rect(0, 0, 10, 10).intersects(Rect(1,1, 12,2)) shouldBe true
    Rect(0, 0, 10, 10).intersects(Rect(-10,-10, -2, -2)) shouldBe false

    Rect(0, 0, 1, 1).intersects(Rect(1,1, 2,2)) shouldBe true
    Rect(0, 0, 10, 10).intersects(Rect(1,1, 12,12)) shouldBe true
    Rect(0, 0, 10, 10).intersects(Rect(-1,-1, 0,0)) shouldBe true
  }

  "Rect" should "be able to inflate" in {
    Rect(0, 0, 10, 10).inflate(2) shouldBe Rect(-2, -2, 12, 12)
    Rect(0, 0, 0, 0).inflate(1) shouldBe Rect(-1, -1, 1, 1)
  }

  "touches" should "fail on non-touching rects" in {
    Rect(0, 3, 1, 4).touches(Rect(0, 0, 1, 1), East) shouldBe false
  }

  "touches" should "fail on incorrect direction of touch" in {
    // Touching, but direction is wrong
    Rect(0, 0, 1, 1).touches(Rect(0, 2, 1, 3), South) shouldBe false
    Rect(0, 0, 1, 1).touches(Rect(0, 2, 1, 3), East) shouldBe false
    Rect(0, 0, 1, 1).touches(Rect(0, 2, 1, 3), West) shouldBe false
  }

  "touches" should "work with different rects touching" in {
    // Happy paths
    Rect(0, 0, 1, 1).touches(Rect(2, 0, 3, 1), West) shouldBe true
    Rect(2, 2, 4, 6).touches(Rect(1, 3, 1, 5), East) shouldBe true
    Rect(1, 3, 1, 5).touches(Rect(2, 2, 4, 6), West) shouldBe true
  }

  "touches" should "work with intersecting rects" in {
    //Intersection also counts as touch with correct direction
    Rect(0, 0, 1, 1).touches(Rect(1,1, 2,2), West) shouldBe true
    Rect(0, 0, 1, 1).touches(Rect(1,1, 2,2), South) shouldBe true
    Rect(0, 0, 1, 1).touches(Rect(1,1, 2,2), East) shouldBe false
    Rect(0, 0, 1, 1).touches(Rect(1,1, 2,2), North) shouldBe false
  }

  "touches" should "work for any direction with emerged rects" in {
    val bigger = Rect(0, 0, 10, 10)
    val smaller = Rect(2, 2, 9, 9)

    bigger.touches(smaller, North) shouldBe true
    bigger.touches(smaller, West) shouldBe true
    bigger.touches(smaller, South) shouldBe true
    bigger.touches(smaller, East) shouldBe true

    smaller.touches(bigger, North) shouldBe true
    smaller.touches(bigger, West) shouldBe true
    smaller.touches(bigger, South) shouldBe true
    smaller.touches(bigger, East) shouldBe true
  }

}
