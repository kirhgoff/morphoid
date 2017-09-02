package org.kirhgoff.morphoid.engine

import org.scalatest._

/**
  * Created by <a href="mailto:kirill.lastovirya@gmail.com">kirhgoff</a> on 2/9/17.
  */
class MorphoidEngineTest extends FlatSpec with Matchers {

  "Monster" should "roam" in {
    val creature = new Creature("monster01", "monster", List(Cell(2, 5)), new HerbivoreSoul("x"))
    val origin = creature.origin
    val newOrigin = creature.next(List()).origin
    origin shouldNot equal(newOrigin)
  }

  "Ooze" should "stay" in {
    val creature = new Creature("ooze01", "ooze", List(Cell(2, 5)), new PlantSoul("x"))
    val origin = creature.origin
    val newOrigin = creature.next(List()).origin
    origin should equal(newOrigin)
  }
}
