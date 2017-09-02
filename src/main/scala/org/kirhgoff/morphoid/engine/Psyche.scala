package org.kirhgoff.morphoid.engine

/**
  * Created by <a href="mailto:kirill.lastovirya@gmail.com">kirhgoff</a> on 2/9/17.
  */
abstract class Psyche (id:String) {
  def next(surroundings:List[Cell], body:Creature):Creature
}

class PredatorSoul(id:String) extends Psyche(id) {
  override def next(surroundings: List[Cell], body: Creature) = {
    body.move(Dice.randomDirection).asInstanceOf[Creature]
  }
}

class PlantSoul(id:String) extends Psyche(id) {
  override def next(surroundings: List[Cell], body: Creature) = body
}
