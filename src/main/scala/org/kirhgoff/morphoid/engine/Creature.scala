package org.kirhgoff.morphoid.engine

// Something active
class Creature(val id:String, val kind:String, var cells:List[Cell], val psyche: Psyche) {
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

  def next(surroundings:List[Cell]): Creature = psyche.next(surroundings, this)
}

object Creature {
  def apply(kind:String, psyche:Psyche, origin:Cell) = {
    new Creature(psyche.id, kind, List(origin), psyche)
  }

  def apply(kind:String, psyche:Psyche, x:Int, y:Int) = {
    new Creature(psyche.id, kind, List(Cell(x, y)), psyche)
  }
}


