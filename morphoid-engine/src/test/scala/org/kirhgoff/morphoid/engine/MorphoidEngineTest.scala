package org.kirhgoff.morphoid.engine

import org.scalatest._
import org.scalamock.scalatest.MockFactory
/**
  * Created by <a href="mailto:kirill.lastovirya@gmail.com">kirhgoff</a> on 2/9/17.
  */
import org.junit.runner.RunWith
import org.scalatest.junit.JUnitRunner

@RunWith(classOf[JUnitRunner])
class MorphoidEngineTest extends FlatSpec with Matchers with MockFactory {

   // ------------------ Creature

  "Creature" should "be able to calculate its origin point" in {
    new Creature("", "", Map(Seed(0, 0), Mover(2, 5))).origin should be(Physical(0, 0))
    new Creature("", "", Map(Mover(2, 5), Seed(0, 0))).origin should be(Physical(0, 0))
    new Creature("", "", Map(Mover(0, 1), Seed(1, 0))).origin should be(Physical(0, 0))
    new Creature("", "", Map(Seed(2, 5))).origin should be(Physical(2, 5))
  }

  "Creature" should "know its bounding rect" in {
    def checkBoundBox(rect: Rect, cells: Map[Physical, CellType]) = {
      rect should equal(new Creature("01", "test", cells).boundingRect)
    }

    checkBoundBox(Rect(2, 3, 2, 3), Map(Seed(2, 3)))
    checkBoundBox(Rect(1, 2, 2, 3), Map(Seed(2, 3), Mover(1, 2)))
    checkBoundBox(Rect(-1, -2, 2, 2), Map(
      Seed(0, 0), Mover(-1, -2), Feeder(2,2)))
  }

  // ------------------ Rect

  "MorphoidEngine" should "provide surroundings" in {
    val engine = MorphoidEngine.createEmpty(10, 10)
    val creature = mock[Creature]
    (creature.cells _).expects().returns(List(Physical(5, 5)))

    engine.surroundings(creature, 1) == List(
      Physical(4, 4), Physical(5, 4), Physical(6, 4),
      Physical(4, 5), Physical(5, 5), Physical(6, 5),
      Physical(4, 6), Physical(5, 6), Physical(6, 6)
    )
  }

  "MorphoidEngine" should "see shrooms without exceptions" in {
    val size = 3

    // Is that all?
    for (
      ox <- 0 to size;
      oy <- 0 to size;
      shx <- 0 to size;
      shy <- 0 to size
    )
    {
      val engine = MorphoidEngine(
        Ooze("ooze", ox, oy, 1), Shroom("shrm", shx, shy)
      ).init()

      val ooze = engine.soulById("ooze")
      val creature = ooze.creature
      ooze.act(engine.surroundings(creature, size))
    }
  }

  // --------------------- Shroom

  "Shroom" should "stay" in {
    val plant = Shroom(2, 5)
    val origin = plant.creature.origin
    MorphoidEngine(plant).tick()
    origin should equal(plant.creature.origin)
  }

  // TODO decide how to split tests
  "Shroom" should "produce energy" in {
    val engine = MorphoidEngine(Shroom(0, 0)).init()
    val initialEnergy = engine.fullEnergy

    engine.tick().fullEnergy should be > initialEnergy
  }


  // ----------------------- Ooze

  "Ooze" should "roam" in {
    val herbivore = Ooze(2, 5, 1)
    MorphoidEngine(herbivore).tick()
    //println("After ----------------------------------->")
    Physical(2, 5) shouldNot equal(herbivore.creature.origin)
  }

  "Ooze" should "move in appropriate time" in {
    val entity = Ooze(2, 5, 3)
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

  private def findCreatureByType(engine: MorphoidEngine, creature: String) =
    engine.getCreatures.find(c => c.kind.equals(creature)).get

  "Ooze" should "move towards shrooms" in {
    val engine = MorphoidEngine(
      Shroom(0, 0),
      Ooze(0, 3, 1)
    ).init()

    engine.creatureType(Physical(0, 0)) shouldEqual "shroom"

    def ooze = findCreatureByType(engine, "ooze")

    engine.tick()
    ooze.origin shouldBe Physical(0, 2) // Moves towards shroom

    engine.tick()
    ooze.origin shouldBe Physical(0, 1) // Moves towards shroom
  }

  "Ooze" should "find best direction" in {
    //One cell
    def checkBestDirection(dx:Int, dy:Int, result:Direction) = {
      val ooze = Ooze(0, 0, 1)
      ooze.bestDirection(Physical(dx, dy)) shouldBe result
    }

    checkBestDirection(-3, 0, West)
    checkBestDirection(2, 3, South)

    checkBestDirection(1, 0, East)
    checkBestDirection(2, 1, East)
    checkBestDirection(-3, -6, North)
    checkBestDirection(0, -3, North)

    //TODO add more tests for multi-cells
  }

  // TODO ask Michal
  "Ooze" should "die without food" in {
    val engine = MorphoidEngine(Shroom(0, 0), Ooze(3, 3, 1)).init()
    val ooze = findCreatureByType(engine, "ooze")

    val systemInitialEnergy = engine.fullEnergy
    val oozeInitialEnergy = ooze.energy

    engine.tick()

    engine.fullEnergy should be > systemInitialEnergy
    ooze.energy should be < oozeInitialEnergy
  }

  "Ooze" should "live near shroom" in {
    val engine = MorphoidEngine(Shroom(0, 0), Ooze(0, 1, 1)).init()
    val ooze = findCreatureByType(engine, "ooze")

    val systemInitialEnergy = engine.fullEnergy
    val oozeInitialEnergy = ooze.energy

    engine.tick()

    engine.fullEnergy should be > systemInitialEnergy
    ooze.energy should be > oozeInitialEnergy
  }

  def creatures(implicit engine: MorphoidEngine): List[Creature] =
    engine.getCreatures.filter(_.kind != "decoy")

  def decoy(implicit engine: MorphoidEngine): List[Creature] =
    engine.getCreatures.filter(_.kind == "decoy")

  "Decoy" should "appear if ooze has died" in {
    implicit val engine = MorphoidEngine(new EnergyBalanceController {
      override def oozeLife = 3
      override def cellDecay = -1
      override def decoyDecay = -0.1
      override def decoyThreshold = 2
    }, Ooze(0, 0, 1))

    // Energy decreased at the end of the tick
    engine.tick()

    creatures should not be empty
    decoy shouldBe empty

    // Creature killed on the third when energy is zero
    engine.tick()

    creatures shouldBe empty
    decoy should not be empty
  }

  "Decoy" should "decay with time" in {
    implicit val engine = MorphoidEngine(new EnergyBalanceController {
      override def oozeLife = 3
      override def cellDecay = -1
      override def decoyDecay = -0.7
      override def decoyThreshold = 2
    }, Ooze(0, 0, 1))

    engine.tick().tick()
    creatures shouldBe empty
    decoy should not be empty

    val initialEnergy = engine.fullEnergy

    engine.tick()

    engine.fullEnergy should be < initialEnergy

    engine.tick()

    decoy shouldBe empty
  }

  "Decoy" should "decay with time 2" in {
    implicit val engine = MorphoidEngine(new EnergyBalanceController {
      override def oozeLife = 1.5
      override def cellDecay = -1
    }, Decoy(0, 0, 1.5))

    engine.tick().tick()
    decoy should not be empty

    engine.tick()
    decoy shouldBe empty
  }

}
