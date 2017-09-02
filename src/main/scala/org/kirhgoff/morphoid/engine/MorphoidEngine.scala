package org.kirhgoff.morphoid.engine

import scala.collection.JavaConverters
import scala.collection.mutable.ListBuffer

/**
  * Created by <a href="mailto:kirill.lastovirya@gmail.com">kirhgoff</a> on 12/3/17.
  */
class MorphoidEngine (initialEntities:List[Entity]) {
  val player = new Player("player01", "player", List(Cell(10, 10)))

  var entities: ListBuffer[Entity] = ListBuffer[Entity]() ++= (player :: initialEntities)

  def getEntities: List[Entity] = entities.toList
  def addEntity(entity:Entity): Unit = entities += entity
  def getEntitiesJava = JavaConverters.asJavaCollection(getEntities)

  def playerMoveSouth() = player.move(South)
  def playerMoveNorth() = player.move(North)
  def playerMoveWest() = player.move(West)
  def playerMoveEast() = player.move(East)

  def playerShoot(direction: Direction) = addEntity(Projectile.make(player.origin, direction))
  def playerShootDown() = playerShoot(South)
  def playerShootUp() = playerShoot(North)
  def playerShootLeft() = playerShoot(West)
  def playerShootRight() = playerShoot(East)

  def creatures:List[Creature] =
    entities.filter(_.isInstanceOf[Creature]).map(_.asInstanceOf[Creature]).toList

  //TODO
  def surroundings(creature: Creature) = null

  def tick() {
    //TODO synchronize
    val newStates = creatures.map(c => c.next(surroundings(c)))
    //TODO separate entities and creatures
    entities = entities --= creatures //Remove old states
    entities ++= newStates //Add new states
  }
}

object MorphoidEngine {
  def createSample = new MorphoidEngine (List(
    new Creature("monster01", "monster", List(Cell(15, 17)), new PredatorSoul("monster01")),
    new Creature("monster02", "monster", List(Cell(17, 15)), new PredatorSoul("monster01")),
    new Creature("ooze01", "ooze", List(Cell(13,12)), new PlantSoul("ooze01")),
    new Creature("ooze02", "ooze", List(Cell(18,19)), new PlantSoul("ooze02")),
    new Creature("ooze03", "ooze", List(Cell(17,14)), new PlantSoul("ooze03"))
  ))
}



