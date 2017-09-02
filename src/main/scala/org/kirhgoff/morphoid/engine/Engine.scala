package org.kirhgoff.morphoid.engine

import java.util.concurrent.atomic.AtomicLong

import scala.collection.mutable

abstract class Direction(val dx:Int, val dy:Int)
case object North extends Direction(0, -1)
case object East extends Direction(1, 0)
case object South extends Direction(0, 1)
case object West extends Direction(-1, 0)

object Direction {
  def clockwise = Array(North, East, South, West)
  def all = clockwise
  def byIndex(i:Int) = clockwise(i)
}

case class Cell(x:Int, y:Int) {
  override def toString: String = s"Cell{x=$x, y=$y}"
}

// Anything on a level
class Entity(val id:String, val kind:String, var cells:List[Cell]) {
  def velocity = 0.0

  def move(direction: Direction) = {
    cells = cells.map(c => Cell(c.x + direction.dx, c.y + direction.dy))
    this
  }

  def origin:Cell = cells match {
    case head::Nil => head
    case head :: tail => tail.foldLeft(head)((cell, next) =>
      Cell(Math.min(cell.x, next.x), Math.max(cell.y, next.y)))
    case list if list.isEmpty => null
  }

  //TODO Context parameter
  def next(surroundings:List[Cell]):Entity = this
}

abstract class Psyche (id:String) {
  def next(surroundings:List[Cell], body:Creature):Creature
}
// Something active
class Creature(id:String, kind:String, cells:List[Cell], psyche: Psyche) extends Entity(id, kind, cells) {
  override def next(surroundings:List[Cell]): Creature = psyche.next(surroundings, this)
}

class Player(id:String, kind:String, cells:List[Cell]) extends Entity(id, kind, cells)
class Projectile(id:String, kind:String, cells:List[Cell], direction:Direction) extends Entity(id, kind, cells)
class Level(width:Int, height:Int, val entities:mutable.MutableList[Entity])

object Projectile {
  def velocity = 1.0/60.0

  val counter = new AtomicLong()

  def make(origin: Cell, direction: Direction): Projectile = {
    new Projectile(s"id${counter.incrementAndGet()}", "projectile",
      List(Cell(origin.x + direction.dx, origin.y + direction.dy)), direction)
  }

}
