package org.kirhgoff.morphoid.engine

import java.util.concurrent.atomic.AtomicLong

// Something active
class Creature(id:String, kind:String, cells:List[Cell], psyche: Psyche) extends Entity(id, kind, cells) {
  override def next(surroundings:List[Cell]): Creature = psyche.next(surroundings, this)
}

class Player(id:String, kind:String, cells:List[Cell]) extends Entity(id, kind, cells)

class Projectile(id:String, kind:String, cells:List[Cell], direction:Direction) extends Entity(id, kind, cells)
object Projectile {
  def velocity = 1.0/60.0
  val counter = new AtomicLong()

  def make(origin: Cell, direction: Direction): Projectile = {
    new Projectile(s"id${counter.incrementAndGet()}", "projectile",
      List(Cell(origin.x + direction.dx, origin.y + direction.dy)), direction)
  }
}

