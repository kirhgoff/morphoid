package org.kirhgoff.morphoid.engine

// Something active
class Creature(val id:String, val kind:String, var cells:List[Cell]) {
  def velocity = 0.0

  def origin:Cell = cells match {
    case head::Nil => head
    case head :: tail => tail.foldLeft(head)((cell, next) =>
      Cell(Math.min(cell.x, next.x), Math.max(cell.y, next.y)))
    case list if list.isEmpty => null
  }

  // Returns true if ready to act
  def move(direction: Direction) = {
    cells = cells.map(c => Cell(c.x + direction.dx, c.y + direction.dy))
    this
  }

  def attack(direction: Direction) = {

  }

  def receive(event: GameEvent) = event match {
    //TODO: move out to separate class
    case CreatureMoves(_,_,direction) => move(direction)
    case CreatureAttacks(_, _, _) => // TODO add State - change state to attacking here
  }

}

object Creature {
  def apply(kind:String, psyche:Psyche, origin:Cell) = {
    new Creature(psyche.id, kind, List(origin))
  }

  def apply(kind:String, psyche:Psyche, x:Int, y:Int) = {
    new Creature(psyche.id, kind, List(Cell(x, y)))
  }
}


