package org.kirhgoff.morphoid.engine

abstract class Direction
case object North extends Direction
case object East extends Direction
case object South extends Direction
case object West extends Direction

object Direction {
  def clockwise = List(North, East, South, West)
  def all = clockwise
}

class Cell(x:Int, y:Int)

class Entity(cells:List[Cell])

class Creature(cells:List[Cell]) extends Entity(cells)
class Player(cells:List[Cell]) extends Entity(cells)
class Projectile(cells:List[Cell], direction:Direction) extends Entity(cells)

class Map(width:Int, height:Int, entities:List[Entity])


