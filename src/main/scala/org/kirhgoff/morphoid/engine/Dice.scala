package org.kirhgoff.morphoid.engine

import scala.util.Random

/**
  * Created by <a href="mailto:kirill.lastovirya@gmail.com">kirhgoff</a> on 2/9/17.
  */
object Dice {
  val random = Random
  def randomDirection = Direction.byIndex(random.nextInt(4))
  def makeId = random.nextString(16)
}
