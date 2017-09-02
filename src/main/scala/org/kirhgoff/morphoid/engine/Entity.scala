package org.kirhgoff.morphoid.engine

/**
  * Created by <a href="mailto:kirill.lastovirya@gmail.com">kirhgoff</a> on 2/9/17.
  */
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
