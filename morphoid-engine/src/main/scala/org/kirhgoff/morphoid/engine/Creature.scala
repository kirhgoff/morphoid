package org.kirhgoff.morphoid.engine

import java.util

import scala.collection.JavaConverters

trait Live {
  def energy: Double
}

// trait Multicellular {
//   def getCellsJava: util.Collection[Cell]
// }

trait CellType {
  override def toString: String = getClass.getSimpleName.toLowerCase
}

object Dummy extends CellType

class Seed extends CellType {
  override def toString: String = "seed"
}

class ShroomSeed extends CellType

class Mover extends CellType

class Feeder extends CellType

//TODO use objects instead of classes + DSL

object Seed {
  def apply(x: Int, y: Int) = Physical(x, y) -> new Seed
}

object Mover {
  def apply(x: Int, y: Int) = Physical(x, y) -> new Mover
}

object Feeder {
  def apply(x: Int, y: Int) = Physical(x, y) -> new Feeder
}

object ShroomSeed {
  def apply(x: Int, y: Int) = Physical(x, y) -> new ShroomSeed
}

// Something active
// TODO rename id to postfix and use kind in id
class Creature(
                val id: String,
                val kind: String,
                val cellsMap: Map[Physical, CellType]) extends Live/* with Multicellular*/ {

  var energy: Double = 0

  def velocity = 0.0

  def cells = cellsMap.keys.toList

  def cellType(physical: Physical) = cellsMap(physical)

  def getEnergy = energy

  def getCellsJava: util.Collection[Physical] = JavaConverters.asJavaCollection(cells)

  def origin: Physical = {
    val result = cells match {
      case head :: Nil => head.makeCopy
      case head :: tail => tail.foldLeft(new Physical(head))((cell, next) =>
        cell.moveTo(Math.min(cell.x, next.x), Math.min(cell.y, next.y)))
      case list if list.isEmpty => null
    }
    //println(s"Origin creature=$result")
    result
  }

  def boundingRect = cells match {
    case head :: Nil => Rect(head, head)
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
    cellsMap.keys.foreach(_.move(direction))
    this
  }

  def attack(direction: Direction) = {

  }

  // TODO figure out what to do with energy
  def updateEnergy(value: Double) = {
    //println(s"Energy changed by $value")
    energy += value
  }

  def receive(event: GameEvent) = event match {
    //TODO: move out to separate class
    case CreatureMoves(_, _, direction) => move(direction)
    case CreatureAttacks(_, _, _) => // TODO add State - change state to attacking here
  }

  override def toString = s"Creature $id $kind cells=$cells life=$energy"

}

object Creature {
  def apply(kind: String, origin: Physical) = {
    new Creature(Dice.makeId(kind), kind, Map(origin -> new Seed))
  }

  def apply(kind: String, psyche: Psyche, x: Int, y: Int) = {
    new Creature(psyche.id, kind, Map(new Physical(x, y) -> new Seed))
  }
}
