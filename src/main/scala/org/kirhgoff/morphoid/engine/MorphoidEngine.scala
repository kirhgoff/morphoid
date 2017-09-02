package org.kirhgoff.morphoid.engine

import scala.collection.JavaConverters
import scala.collection.mutable.ListBuffer

/**
  * Created by <a href="mailto:kirill.lastovirya@gmail.com">kirhgoff</a> on 12/3/17.
  */
class MorphoidEngine (initialEntities:List[Creature]) {
  val player = Creature("player", new PlantSoul("player01"), 10, 10)
  var creatures: ListBuffer[Creature] = ListBuffer[Creature]() ++= (player :: initialEntities)

  def getEntities: List[Creature] = creatures.toList
  def addEntity(creature:Creature): Unit = creatures += creature
  def getEntitiesJava = JavaConverters.asJavaCollection(getEntities)

  def playerMoveSouth() = player.move(South)
  def playerMoveNorth() = player.move(North)
  def playerMoveWest() = player.move(West)
  def playerMoveEast() = player.move(East)

  def playerShoot(direction: Direction) =
    addEntity(Creature("projectile", new Projectile("projectile01", direction), player.origin))
  def playerShootDown() = playerShoot(South)
  def playerShootUp() = playerShoot(North)
  def playerShootLeft() = playerShoot(West)
  def playerShootRight() = playerShoot(East)

  //TODO synchronize
  def tick() {
    //TODO add new & delete dead
    creatures = creatures.map(c => c.next(surroundings(c)))
  }

  //TODO
  def surroundings(creature: Creature):List[Cell] = List()
}

object MorphoidEngine {
  def createSample = new MorphoidEngine (List(
    Creature("monster", new HerbivoreSoul("monster01"), 15, 17),
    Creature("monster", new HerbivoreSoul("monster02"), 17, 15),
    Creature("ooze", new PlantSoul("ooze01"), 13, 19),
    Creature("ooze", new PlantSoul("ooze02"), 19, 13),
    Creature("ooze", new PlantSoul("ooze03"), 16, 12)
  ))
}



