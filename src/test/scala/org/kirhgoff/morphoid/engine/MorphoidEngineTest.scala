package org.kirhgoff.morphoid.engine

import org.scalatest._
import org.scalamock.scalatest.MockFactory
/**
  * Created by <a href="mailto:kirill.lastovirya@gmail.com">kirhgoff</a> on 2/9/17.
  */
class MorphoidEngineTest extends FlatSpec with Matchers with MockFactory {

  "Creature" should "be able to calculate its origin point" in {
    new Creature("", "", List(Cell(0, 0), Cell(2, 5))).origin should be(Cell(0, 5))
    new Creature("", "", List(Cell(2, 5))).origin should be(Cell(2, 5))
  }

  "Monster" should "roam" in {
    val herbivore = Herbivore(2, 5, 1)
    val origin = herbivore.creature.origin
    MorphoidEngine(herbivore).tick()
    origin shouldNot equal(herbivore.creature.origin)
  }

  "Ooze" should "stay" in {
    val plant = Plant(2, 5)
    val origin = plant.creature.origin
    MorphoidEngine(plant).tick()
    origin should equal(plant.creature.origin)
  }

  "Moving creature" should "move in appropriate time" in {
    val entity = Herbivore(2, 5, 3)
    val creature = entity.creature
    val origin = creature.origin
    val engine = MorphoidEngine(entity)
    engine.tick()
    origin should equal(creature.origin)
    engine.tick()
    origin should equal(creature.origin)
    engine.tick() //Move here
    origin shouldNot equal(creature.origin)
    val newOrigin = creature.origin
    engine.tick()
    newOrigin should equal(creature.origin)
  }

  "Creature" should "know its bounding rect" in {
    checkBoundBox(Rect(2, 3, 2, 3), List(Cell(2, 3)))
    checkBoundBox(Rect(1, 2, 2, 3), List(Cell(2, 3), Cell(1, 2)))
    checkBoundBox(Rect(-1, -2, 2, 2), List(Cell(0, 0), Cell(-1, -2), Cell(2,2)))
  }

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

  "Ooze" should "move towards shrooms" in {
    val engine = MorphoidEngine(
      Plant(0, 0),
      Herbivore(0, 3, 1)
    )
    engine.tick()
    val ooze = engine.getEntities.find(c => c.kind.equals("ooze")).get
    ooze.origin shouldBe Cell(0, 2) // Moves towards shroom
  }



  private def checkBoundBox(rect: Rect, cells: List[Cell]) = {
    rect should equal(new Creature("01", "test", cells).boundingRect)
  }
}
