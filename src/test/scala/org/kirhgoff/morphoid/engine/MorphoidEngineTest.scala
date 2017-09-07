package org.kirhgoff.morphoid.engine

import org.scalatest._
import org.scalamock.scalatest.MockFactory
/**
  * Created by <a href="mailto:kirill.lastovirya@gmail.com">kirhgoff</a> on 2/9/17.
  */
class MorphoidEngineTest extends FlatSpec with Matchers with MockFactory {

  "Creature" should "be able to calculate its origin point" in {
    new Creature("", "", List(Cell(0, 0), Cell(2, 5)), null).origin should be(Cell(0, 5))
    new Creature("", "", List(Cell(2, 5)), null).origin should be(Cell(2, 5))
  }

  "Monster" should "roam" in {
    val creature = new Creature("monster01", "monster", List(Cell(2, 5)), new HerbivoreSoul("x", 1))
    val origin = creature.origin
    val newOrigin = creature.act(List()).origin
    origin shouldNot equal(newOrigin)
  }

  "Ooze" should "stay" in {
    val creature = new Creature("ooze01", "ooze", List(Cell(2, 5)), new PlantSoul("x"))
    val origin = creature.origin
    val newOrigin = creature.act(List()).origin
    origin should equal(newOrigin)
  }

  "Moving creature" should "move in appropriate time" in {
    val creature = new Creature("slime1", "slime", List(Cell(2, 5)), new HerbivoreSoul("x", 3))
    val origin = creature.origin
    val engine = MorphoidEngine(List(creature))
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







}
