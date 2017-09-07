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
class MorphoidEngine (initialEntities:List[Creature]) {
  val GOD_ENGINE = "GOD ENGINE v.01"

  val player = initialEntities.head
  var creatures: ListBuffer[Creature] = ListBuffer[Creature]() ++ initialEntities
  var souls:ListBuffer[Psyche] = creatures.map(_.psyche)

  val creaturesIndex = mutable.Map[String, Creature]()

  //TODO implement surroundings properly
  def surroundings(creature: Creature):List[Cell] = List()

  // UI Interface
  def getEntities: List[Creature] = creatures.toList
  def getEntitiesJava = JavaConverters.asJavaCollection(getEntities)

  def addEntity(creature:Creature) {
    creatures += creature
    creaturesIndex[creature.id] = creature
  }

  def tick() {
    val actions = souls.map(p => p.act(surroundings(p.creature)))
    //val validated = validate(actions)
    actions.foreach(execute(_))
  }

  def execute(events: List[GameEvent]) = events match {
    case Nil => _
    case List() => _
    case event :: rest => event match {
      case CreatureMoves(_, id, direction) => {
        val creature = creaturesIndex[id]
        
      }
    }

  }

  // User Input interface
  def playerMoves(direction: Direction) =
    player.receive(CreatureMoves(GOD_ENGINE, direction))

  def playerShoot(direction: Direction) = {
    player.receive(CreatureAttacks(GOD_ENGINE, direction))
    addEntity(Creature("projectile", new Projectile(s"projectile${Dice.makeId}", direction, 5), player.origin))
  }
}

object MorphoidEngine {
  def createSample(playerInputState: PlayerInputState) = new MorphoidEngine (List(
    Creature("player", new PlayerSoul("player01", playerInputState, 5), 10, 10),
    Creature("monster", new HerbivoreSoul("monster01", 40), 3, 5),
    Creature("monster", new HerbivoreSoul("monster02", 40), 15, 15),
    Creature("ooze", new PlantSoul("ooze01"), 1, 8),
    Creature("ooze", new PlantSoul("ooze02"), 6, 13),
    Creature("ooze", new PlantSoul("ooze03"), 16, 12)
  ))

  def apply(initialValues: List[Creature]) = new MorphoidEngine(initialValues)
}



