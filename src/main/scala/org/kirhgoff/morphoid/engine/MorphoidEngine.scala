package org.kirhgoff.morphoid.engine

import scala.collection.JavaConverters._
import scala.collection.mutable

/**
  * Created by <a href="mailto:kirill.lastovirya@gmail.com">kirhgoff</a> on 12/3/17.
  */
class MorphoidEngine (initialEntities:List[Entity]) {
  val player = new Player("player01", "player", List(Cell(10, 10)))
  val entities = mutable.MutableList(player :: initialEntities)

  def getEntities: List[Entity] = entities
  def addEntity(entity:Entity): Unit = entities +== entity
  def getEntitiesJava = getEntities.asJava

  def playerMovesSouth = player.move(South)
  def playerMovesNorth = player.move(North)
  def playerMovesWest = player.move(West)
  def playerMovesEast = player.move(East)

  def playerShoots(direction: Direction) = addEntity(Projectile.make(player.origin, direction))
  def playerShootsDown = playerShoots(South)
  def playerShootsUp = playerShoots(North)
  def playerShootsLeft = playerShoots(West)
  def playerShootsRight = playerShoots(East)
}

object MorphoidEngine {
  def createSample = new MorphoidEngine (List(
    new Creature("monster01", "monster", List(Cell(15, 17))),
    new Creature("monster02", "monster", List(Cell(17, 15)))
  ))
}
