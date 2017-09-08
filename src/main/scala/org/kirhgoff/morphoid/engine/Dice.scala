package org.kirhgoff.morphoid.engine

import java.util.concurrent.atomic.AtomicLong

import scala.util.Random

/**
  * Created by <a href="mailto:kirill.lastovirya@gmail.com">kirhgoff</a> on 2/9/17.
  */
object Dice {
  val idCounter = new AtomicLong()
  val random = Random

  def randomDirection = Direction.byIndex(random.nextInt(4))
  //TODO format with zeros
  def makeId(prefix:String = "") = prefix + idCounter.incrementAndGet()
}
