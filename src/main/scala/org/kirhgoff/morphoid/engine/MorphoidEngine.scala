package org.kirhgoff.morphoid.engine

import org.kirhgoff.morphoid.PlayerInputState

import scala.collection.{JavaConverters, mutable}
import scala.collection.mutable.ListBuffer

abstract class GameEvent(sourceId:String, targetId:String)
case class CreatureMoves(sourceId:String, targetId:String, direction:Direction) extends GameEvent(sourceId, targetId)
case class CreatureAttacks(sourceId:String, targetId:String, direction:Direction) extends GameEvent(sourceId, targetId)
case class CreatureObserve(sourceId:String, targetId:String, surroundings:List[Cell]) extends GameEvent(sourceId, targetId)

/**
  * Not thread safe
  *
  * Created by <a href="mailto:kirill.lastovirya@gmail.com">kirhgoff</a> on 12/3/17.
  */
class MorphoidEngine (initialEntities:List[Psyche]) {
  val GOD_ENGINE = "GOD ENGINE v.01"

  val player = initialEntities.head
  var creatures =  mutable.Map[String, Creature](initialEntities map (p => p.id -> p.creature): _*)
  val souls = mutable.Map[String, Psyche](initialEntities map (p => p.id -> p): _*)

  //TODO implement surroundings properly
  def surroundings(creature: Creature):List[Cell] = List()

  // UI Interface
  def getEntities: List[Creature] = creatures.values.toList
  def getEntitiesJava = JavaConverters.asJavaCollection(getEntities)

  private def addEntity(psyche: Psyche) = {
    val creature = psyche.creature
    creatures(creature.id) = creature
    souls(psyche.id) = psyche
  }

  def tick() {
    val actions = souls.values.map(p => {
      if (p.tick) p.act(surroundings(p.creature))
      else List()
    })
    //val validated = validate(actions)
    actions.foreach(execute)
  }

  def execute(events: List[GameEvent]) = events match {
    case Nil =>
    case List() =>
    case event :: rest => event match {
      case CreatureMoves(_, id, direction) => {
        creatures(id).move(direction)
      }
      case CreatureAttacks(_, id, direction) => {
        val creature = creatures(id)
        creature.attack(direction)
        val newId = Dice.makeId("pew")
        val projectileOrigin = creature.origin.nextTo(direction)
        val projectile = new Creature(newId, "projectile", List(projectileOrigin))
        addEntity(new Projectile(newId, direction, 5, projectile))
      }
    }

  }
}

object MorphoidEngine {
  def createSample(playerInputState: PlayerInputState) = new MorphoidEngine (List(
    PlayerSoul(playerInputState, 10, 10, 5),
    Herbivore(3, 5, 40),
    Herbivore(15, 15, 60),
    Herbivore(9, 11, 50),
    Plant(1, 8),
    Plant(6, 13),
    Plant(16, 12)
  ))

}



