package org.kirhgoff.morphoid.engine

import scala.collection.mutable

abstract class Direction(val dx:Int, val dy:Int)
case object North extends Direction(0, -1)
case object East extends Direction(1, 0)
case object South extends Direction(0, 1)
case object West extends Direction(-1, 0)

object Direction {
  def clockwise = List(North, East, South, West)
  def all = clockwise
}

case class Cell(x:Int, y:Int) {
  override def toString: String = s"Cell{x=$x, y=$y}"
}

class Entity(val id:String, val kind:String, var cells:List[Cell]) {
  def move(direction: Direction):Unit = {
    cells = cells.map(c => Cell(c.x + direction.dx, c.y + direction.dy))
    println(s"cells=$cells")
  }

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


