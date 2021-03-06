package org.kirhgoff.morphoid.engine

/**
  * Created by <a href="mailto:kirill.lastovirya@gmail.com">kirhgoff</a> on 14/10/17.
  */
class EnergyBalanceController {
  def seedGrowth = 0
  def cellDecay = -0.01
  def shroomIncrease = 1

  def decoyDecay = cellDecay
  def decoyThreshold = shroomLife/10
  def completeDecoy = 0

  // Lives
  def shroomLife = 10
  def oozeLife = shroomLife * 20
  def playerLife = oozeLife * 1000
  def projectile = shroomLife / 4

}

object EnergyBalanceController {
  def generic() = new EnergyBalanceController
}
