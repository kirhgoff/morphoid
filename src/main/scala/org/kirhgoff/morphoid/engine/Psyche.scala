package org.kirhgoff.morphoid.engine

import org.kirhgoff.morphoid.PlayerInputState

import scala.collection.mutable.ListBuffer

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

abstract class Psyche (val id:String, val velocity: Int, val creature: Creature) extends Movable {
  def sight:Int = 0
  def act(surroundings:List[Cell]):List[GameEvent]
}

class HerbivoreSoul(id:String, velocity:Int, creature:Creature) extends Psyche(id, velocity, creature) {
  override def sight = 2
  override def act(surroundings: List[Cell]) = {
    List(CreatureMoves(id, creature.id, Dice.randomDirection))
  }
}

class PlantSoul(id:String, creature:Creature) extends Psyche(id, Movable.ZeroVelocity, creature) {
  override def act(surroundings: List[Cell]) = List()
}

class Projectile(id:String, direction:Direction, velocity:Int, creature:Creature) extends Psyche(id, velocity, creature) {
  override def act(surroundings: List[Cell]) = {
    List(CreatureMoves(id, creature.id, direction))
  }
}

class PlayerSoul(id:String, input: PlayerInputState, velocity: Int, creature: Creature) extends Psyche(id, velocity, creature) {
  override def act(surroundings: List[Cell]) = {
    //println("PS read " + input)
    var events = ListBuffer[GameEvent]()

    if(input.isMovingLeft) events += CreatureMoves(id, creature.id, West)
    if(input.isMovingRight) events += CreatureMoves(id, creature.id, East)
    if(input.isMovingUp) events += CreatureMoves(id, creature.id, North)
    if(input.isMovingDown) events += CreatureMoves(id, creature.id, South)

    if(input.isShootingUp) events += CreatureAttacks(id, creature.id, North)
    if(input.isShootingDown) events += CreatureAttacks(id, creature.id, South)
    if(input.isShootingLeft) events += CreatureAttacks(id, creature.id, West)
    if(input.isShootingRight) events += CreatureAttacks(id, creature.id, East)

    events.toList
  }
}

object PlayerSoul {
  def apply(playerInputState: PlayerInputState, x:Int, y:Int, velocity:Int) = {
    val id: String = Dice.makeId("player")
    new PlayerSoul(id, playerInputState, velocity, new Creature(id, "player", List(Cell(x, y))))
  }
}

object Plant {
  def apply(x:Int, y:Int) = {
    val id: String = Dice.makeId("ooze")
    new PlantSoul(id, new Creature(id, "ooze", List(Cell(x, y))))
  }
}

object Herbivore {
  def apply(x:Int, y:Int, velocity:Int) = {
    val id: String = Dice.makeId("monster")
    new HerbivoreSoul(id, velocity, new Creature(id, "monster", List(Cell(x, y))))
  }
}
