package org.kirhgoff.morphoid.engine

/**
  * Created by <a href="mailto:kirill.lastovirya@gmail.com">kirhgoff</a> on 2/9/17.
  */

trait Live {
  def energy:Long

  def addEnergy(value:Long) = energy += value
  def subtractEnergy(value:Long) = energy -= value
  def isAlive = energy > 0
}

trait Moving {
  var loops:Long = 0
  def velocity:Int //How many loops needed to act

  def tick:Boolean = {
    loops += 1
    velocity != 0 && loops % velocity == 0
  }
}

abstract class Psyche (val id:String, val energy:Long, val velocity: Int) extends Moving{
  def sight:Int = 0
  def act(surroundings:List[Cell], body:Creature):Creature
}

class HerbivoreSoul(id:String, energy:Long, velocity:Int) extends Psyche(id, energy, velocity) {
  override def sight = 2
  override def act(surroundings: List[Cell], body: Creature) = {
    body.move(Dice.randomDirection)
  }
}

class PlantSoul(id:String) extends Psyche(id, 1, 0) {
  override def act(surroundings: List[Cell], body: Creature) = body
}

class Projectile(id:String, direction:Direction, velocity:Int, charge:Long) extends Psyche(id, charge, velocity) {
  def act(surroundings: List[Cell], body: Creature): Creature = {
    body.move(direction)
  }
}
