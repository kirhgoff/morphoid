package org.kirhgoff.morphoid.engine

import scala.collection.JavaConverters._

/**
  * Created by <a href="mailto:kirill.lastovirya@gmail.com">kirhgoff</a> on 12/3/17.
  */
class MorphoidEngine {
  val player = new Player("player01", "player", List(Cell(10, 10)))

  def getEntities: List[Entity] = {
    List(
      player,
      new Creature("monster01", "monster", List(Cell(15, 17))),
      new Creature("monster02", "monster", List(Cell(17, 15)))
    )
  }

  def getEntitiesJava = getEntities.asJava

  def playerMovesSouth = player.move(South)
  def playerMovesNorth = player.move(North)
  def playerMovesWest = player.move(West)
  def playerMovesEast = player.move(East)
}

object MorphoidEngine {
  def createSample = new MorphoidEngine
}
