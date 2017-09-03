package org.kirhgoff.morphoid.engine

import java.util.concurrent.atomic.AtomicLong

/**
  * Created by <a href="mailto:kirill.lastovirya@gmail.com">kirhgoff</a> on 2/9/17.
  */

/**
  *
  */
trait Moving {
  val loops:AtomicLong = new AtomicLong()
  def velocity:Int //How many loops needed to act
  def mayAct = velocity != 0 && loops.get() % velocity == 0
  def tick = loops.incrementAndGet()
}

abstract class Psyche (val id:String, val velocity: Int) extends Moving{
  def sight:Int = 0
  def next(surroundings:List[Cell], body:Creature):Creature
}

class HerbivoreSoul(id:String, velocity:Int) extends Psyche(id, velocity) {
  override def sight = 2
  override def next(surroundings: List[Cell], body: Creature) = {
    body.move(Dice.randomDirection)
  }
}

class PlantSoul(id:String) extends Psyche(id, 0) {
  override def next(surroundings: List[Cell], body: Creature) = body
}

class Projectile(id:String, direction:Direction, velocity:Int) extends Psyche(id, velocity) {
  def next(surroundings: List[Cell], body: Creature): Creature = {
    body.move(direction)
  }
}
