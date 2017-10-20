package org.kirhgoff.morphoid.engine

/**
  * Created by <a href="mailto:kirill.lastovirya@gmail.com">kirhgoff</a> on 14/10/17.
  */
object EnergyBalance {
  def cellDecay = -0.01
  def shroomIncrease = 1

  // Lifes
  def shroomLife = 5
  def oozeLife = shroomLife * 20
  def playerLife = oozeLife * 1000
}
