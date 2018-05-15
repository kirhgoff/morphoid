package org.kirhgoff.morphoid.engine

/**
  * Created by <a href="mailto:kirill.lastovirya@gmail.com">kirhgoff</a> on 10/11/17.
  */
class Decoy(deadCreature: Creature, initialEnergy: Double)
    extends Creature(Dice.makeId(), "decoy",
      deadCreature.cells.map(_ -> DecayingCell).toMap) {

  energy = initialEnergy

  override def updateEnergy(growth:Double) = energy += growth
  override def toString = s"Decoy($energy)"
}
