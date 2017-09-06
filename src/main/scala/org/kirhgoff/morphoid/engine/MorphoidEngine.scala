package org.kirhgoff.morphoid.engine

import org.kirhgoff.morphoid.PlayerInputState

import scala.collection.JavaConverters
import scala.collection.mutable.ListBuffer

abstract class GameEvent(sourceId:String)
case class CreatureAttacks(sourceId:String, direction:Direction) extends GameEvent(sourceId)
case class CreatureMoves(sourceId:String, direction:Direction) extends GameEvent(sourceId)
case class CreatureObserve(sourceId:String, surroundings:List[Cell]) extends GameEvent(sourceId)

/**
  * Not thread safe
  *
  * Created by <a href="mailto:kirill.lastovirya@gmail.com">kirhgoff</a> on 12/3/17.
  */
class MorphoidEngine (initialEntities:List[Creature]) {
  val GOD_ENGINE = "GOD ENGINE v.01"

  val player = initialEntities.filter(e => "player".equals(e.kind)).head
  var creatures: ListBuffer[Creature] = ListBuffer[Creature]() ++ initialEntities

  //TODO implement surroundings properly
  def surroundings(creature: Creature):List[Cell] = List()

  // UI Interface
  def getEntities: List[Creature] = creatures.toList
  def getEntitiesJava = JavaConverters.asJavaCollection(getEntities)
  def addEntity(creature:Creature): Unit = creatures += creature
  def tick() {
    //TODO add new & delete dead
    creatures = creatures.map(c => {
      if (c.tick) c.act(surroundings(c)) else c
    })
  }

  // User Input interface
  def playerStartMoving(direction: Direction) =
    player.receive(CreatureMoves(GOD_ENGINE, direction))

  def playerShoot(direction: Direction) = {
    player.receive(CreatureAttacks(GOD_ENGINE, direction))
    addEntity(Creature("projectile", new Projectile(s"projectile${Dice.makeId}", direction, 5), player.origin))
  }
}

object MorphoidEngine {
  def createSample(playerInputState: PlayerInputState) = new MorphoidEngine (List(
    Creature("player", new PlayerSoul("player01", playerInputState, 10), 10, 10),
    Creature("monster", new HerbivoreSoul("monster01", 40), 15, 17),
    Creature("monster", new HerbivoreSoul("monster02", 40), 17, 15),
    Creature("ooze", new PlantSoul("ooze01"), 13, 19),
    Creature("ooze", new PlantSoul("ooze02"), 19, 13),
    Creature("ooze", new PlantSoul("ooze03"), 16, 12)
  ))

  def apply(initialValues: List[Creature]) = new MorphoidEngine(initialValues)
}



