package org.kirhgoff.morphoid.engine

import scala.collection.mutable

abstract class Direction
case object North extends Direction
case object East extends Direction
case object South extends Direction
case object West extends Direction

object Direction {
  def clockwise = List(North, East, South, West)
  def all = clockwise
}

case class Cell(x:Int, y:Int)

class Entity(val id:String, val kind:String, val cells:List[Cell]) {
  def origin:Cell = cells match {
    case head :: tail => tail.foldLeft(head)((cell, next) =>
      Cell(Math.min(cell.x, next.x), Math.max(cell.y, next.y)))
    case head::Nil => head
    case list if list.isEmpty => null
  }

}

class Creature(id:String, kind:String, cells:List[Cell]) extends Entity(id, kind, cells)
class Player(id:String, kind:String, cells:List[Cell]) extends Entity(id, kind, cells)
class Projectile(id:String, kind:String, cells:List[Cell], direction:Direction) extends Entity(id, kind, cells)

class Level(width:Int, height:Int, val entities:mutable.MutableList[Entity])


