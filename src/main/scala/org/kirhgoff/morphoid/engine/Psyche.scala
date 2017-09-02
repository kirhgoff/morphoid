package org.kirhgoff.morphoid.engine

/**
  * Created by <a href="mailto:kirill.lastovirya@gmail.com">kirhgoff</a> on 2/9/17.
  */
abstract class Psyche (val id:String) {
  def sight:Int = 0
  def next(surroundings:List[Cell], body:Creature):Creature
}

class HerbivoreSoul(id:String) extends Psyche(id) {
  override def sight = 2
  override def next(surroundings: List[Cell], body: Creature) = {
    body.move(Dice.randomDirection).asInstanceOf[Creature]
  }
}

class PlantSoul(id:String) extends Psyche(id) {
  override def next(surroundings: List[Cell], body: Creature) = body
}

class Projectile(id:String, direction:Direction) extends Psyche(id) {
  def next(surroundings: List[Cell], body: Creature): Creature = {
    body.move(direction)
  }
}
