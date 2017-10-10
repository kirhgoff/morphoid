package org.kirhgoff.morphoid.engine

import org.kirhgoff.morphoid.PlayerInputState

import scala.collection.mutable.ListBuffer

/**
  * Created by <a href="mailto:kirill.lastovirya@gmail.com">kirhgoff</a> on 2/9/17.
  */

trait Movable {
  var loops:Long = 0
  def velocity:Int //How many loops needed to act

  def readyToAct:Boolean = {
    loops += 1
    velocity != 0 && loops % velocity == 0
  }
}

object Movable {
  val ZeroVelocity:Int = 0
}

trait EngineAware {
  var engine:Lore = null
  def setEngine(engine:Lore) = this.engine = engine
}
// ---------------------

abstract class Psyche (val id:String, val velocity: Int, val creature: Creature)
  extends Movable with EngineAware {
  def sight:Int = 0
  def act(surroundings:List[Physical]):List[GameEvent]
}

// ---------------------

class HerbivoreSoul(id:String, velocity:Int, creature:Creature) extends Psyche(id, velocity, creature) {
  override def sight = 3

  override def act(surroundings: List[Physical]) = {
    val direction = surroundings.find(shroom) match {
      case Some(cell) => bestDirection(cell)
      case None => Dice.randomDirection
    }
    List(CreatureMoves(id, creature.id, direction))
  }

  def shroom(cell:Physical):Boolean = {
    //engine.kindsInside(cell).contains("shroom")
    engine.kindsInside(cell).contains("shroom")
  }

  def bestDirection(cell: Physical) = {
    val origin = creature.origin
    Direction.byDelta(cell.x - origin.x, cell.y - origin.y)
  }

  override def toString = s"Herbivory $id v=$velocity c=$creature"
}

// ---------------------

class PlantSoul(id:String, creature:Creature) extends Psyche(id, Movable.ZeroVelocity, creature) {
  override def act(surroundings: List[Physical]) = List()
}

// ---------------------

class Projectile(id:String, direction:Direction, velocity:Int, creature:Creature) extends Psyche(id, velocity, creature) {
  override def act(surroundings: List[Physical]) = {
    List(CreatureMoves(id, creature.id, direction))
  }
}

// ---------------------

class PlayerSoul(id:String, input: PlayerInputState, velocity: Int, creature: Creature) extends Psyche(id, velocity, creature) {
  override def act(surroundings: List[Physical]) = {
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
    new PlayerSoul(id, playerInputState, velocity, new Creature(id, "player", 100, Map(Physical(x, y) -> "seed")))
  }
}

object Plant {
  def apply(x:Int, y:Int):Psyche = {
    val id: String = Dice.makeId("shroom")
    new PlantSoul(id, new Creature(id, "shroom", 5, Map(Physical(x, y) -> "seed")))
  }
}

object Herbivore {
  def apply(x:Int, y:Int, velocity:Int) = {
    val id: String = Dice.makeId("ooze")
    new HerbivoreSoul(id, velocity, new Creature(id, "ooze", 50, Map(Physical(x, y) -> "seed")))
  }
}
