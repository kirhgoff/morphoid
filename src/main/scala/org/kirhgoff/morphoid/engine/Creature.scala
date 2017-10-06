package org.kirhgoff.morphoid.engine

trait Live {
  def energy:Int
  def isAlive = energy > 0
}

// Something active
// TODO rename id to postfix and use kind in id
class Creature(val id:String, val kind:String, var energy:Int, var cells:List[Cell]) extends Live {
  def velocity = 0.0

  def origin:Cell = cells match {
    case head::Nil => head
    case head :: tail => tail.foldLeft(head)((cell, next) =>
      Cell(Math.min(cell.x, next.x), Math.max(cell.y, next.y)))
    case list if list.isEmpty => null
  }

  def boundingRect = cells match {
    case head::Nil => Rect(head, head)
    case head :: tail => tail.foldLeft(Rect(head, head))((rect, next) =>
      Rect(
        Math.min(rect.x1, next.x),
        Math.min(rect.y1, next.y),
        Math.max(rect.x2, next.x),
        Math.max(rect.y2, next.y)
      ))
    case list if list.isEmpty => null
  }

  def move(direction: Direction) = {
    cells = cells.map(c => Cell(c.x + direction.dx, c.y + direction.dy))
    this
  }

  def attack(direction: Direction) = {

  }

  def updateEnergy(value:Int) = energy -= value

  def receive(event: GameEvent) = event match {
    //TODO: move out to separate class
    case CreatureMoves(_,_,direction) => move(direction)
    case CreatureAttacks(_, _, _) => // TODO add State - change state to attacking here
  }

  override def toString = s"Creature $id $kind cells=$cells"

}

object Creature {
  def apply(kind:String, origin:Cell) = {
    new Creature(Dice.makeId(kind), kind, 10, List(origin))
  }

  def apply(kind:String, psyche:Psyche, x:Int, y:Int) = {
    new Creature(psyche.id, kind, 10, List(Cell(x, y)))
  }
}
