package org.kirhgoff.morphoid.engine

/**
  * Created by <a href="mailto:kirill.lastovirya@gmail.com">kirhgoff</a> on 14/10/17.
  */
class EnergyBalanceController {
  def seedGrowth = 0.0
  def cellDecay = -0.01
  def shroomIncrease = 1.0

  def decoyDecay = cellDecay/100
  def decoyThreshold = shroomLife/10
  def completeDecoy = 0.0

  // Lives
  def shroomLife = 10.0
  def oozeLife = shroomLife * 20
  def playerLife = oozeLife * 1000
  def projectile = shroomLife / 4

  // Attacks
  def oozeAttack = shroomLife / 4

}

object EnergyBalanceController {
  def simple() = new EnergyBalanceController

  def realistic() = new EnergyBalanceController {
    override def oozeLife = shroomLife * 2
    override def decoyThreshold = oozeLife / 4
  }
}
