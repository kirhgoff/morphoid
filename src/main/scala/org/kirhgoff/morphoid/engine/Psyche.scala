package org.kirhgoff.morphoid.engine

import org.kirhgoff.morphoid.PlayerInputState

/**
  * Created by <a href="mailto:kirill.lastovirya@gmail.com">kirhgoff</a> on 2/9/17.
  */

//trait Live {
//  def energy:Long
//
//  def addEnergy(value:Long) = energy += value
//  def subtractEnergy(value:Long) = energy -= value
//  def isAlive = energy > 0
//}

object Movable {
  val ZeroVelocity:Int = 0
}

trait Movable {
  var loops:Long = 0
  def velocity:Int //How many loops needed to act

  def tick:Boolean = {
    loops += 1
    velocity != 0 && loops % velocity == 0
  }
}

abstract class Psyche (val id:String, val velocity: Int) extends Movable {
  def sight:Int = 0
  def act(surroundings:List[Cell], body:Creature):Creature
}

class HerbivoreSoul(id:String, velocity:Int) extends Psyche(id, velocity) {
  override def sight = 2
  override def act(surroundings: List[Cell], body: Creature) = {
    body.move(Dice.randomDirection)
  }
}

class PlantSoul(id:String) extends Psyche(id, Movable.ZeroVelocity) {
  override def act(surroundings: List[Cell], body: Creature) = body
}

class Projectile(id:String, direction:Direction, velocity:Int) extends Psyche(id, velocity) {
  override def act(surroundings: List[Cell], body: Creature): Creature = {
    body.move(direction)
  }
}

class PlayerSoul(id:String, input: PlayerInputState, velocity: Int) extends Psyche(id, velocity) {
  override def act(surroundings: List[Cell], body: Creature) = {
    //println("PS read " + input)
    if(input.isMovingLeft) body.move(West)
    if(input.isMovingRight) body.move(East)
    if(input.isMovingUp) body.move(North)
    if(input.isMovingDown) body.move(South)

    if(input.isShootingUp) body.attack(North)
    if(input.isShootingDown) body.attack(South)
    if(input.isShootingLeft) body.attack(West)
    if(input.isShootingRight) body.attack(East)
    body
  }
}
