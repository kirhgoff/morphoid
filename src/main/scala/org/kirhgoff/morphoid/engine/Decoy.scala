package org.kirhgoff.morphoid.engine

/**
  * Created by <a href="mailto:kirill.lastovirya@gmail.com">kirhgoff</a> on 10/11/17.
  */
//TODO make creature
class Decoy(var energy:Double) {
  def updateEnergy(growth:Double) = energy += growth

  override def toString = s"Decoy($energy)"
}
