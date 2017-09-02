package org.kirhgoff.morphoid.engine

import org.scalatest._

/**
  * Created by <a href="mailto:kirill.lastovirya@gmail.com">kirhgoff</a> on 2/9/17.
  */
class MorphoidEngineTest extends FlatSpec with Matchers {

  "Monster" should "roam" in {
    val monster = new Creature("monster01", "monster", List(Cell(2, 5)), new PredatorSoul("x"))
    val origin = monster.origin
    val newOrigin = monster.next(List()).origin
    origin shouldNot equal(newOrigin)
  }
}
