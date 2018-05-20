package org.kirhgoff.morphoid.engine

/**
  * Created by <a href="mailto:kirill.lastovirya@gmail.com">kirhgoff</a> on 10/11/17.
  */
class Decoy(id: String,
            kind: String,
            cellsMap: Map[Physical, CellType]) extends Creature(id, kind, cellsMap) {
  override def toString = s"[Decoy $id $energy]"

  def withEnergy(initial: Double) = {
    updateEnergy(initial)
    this
  }
}


object Decoy {
  def fromDead(deadCreature: Creature): Psyche = new PlantSoul(
    Dice.makeId(),
    new Decoy (Dice.makeId (), "decoy", deadCreature.cells.map (_-> DecayingCell).toMap)
      .withEnergy (deadCreature.energy)
  )

  def apply(x: Int, y: Int, energy: Double): Psyche = new PlantSoul(
    Dice.makeId(),
    new Decoy (Dice.makeId (), "decoy", Map(Physical(x, y) -> DecayingCell))
      .withEnergy(energy)
  )
}
