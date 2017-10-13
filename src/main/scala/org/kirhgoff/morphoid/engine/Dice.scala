package org.kirhgoff.morphoid.engine

import java.util.concurrent.atomic.AtomicLong

import scala.util.Random

/**
  * Created by <a href="mailto:kirill.lastovirya@gmail.com">kirhgoff</a> on 2/9/17.
  */
object Dice {
  def randomOne() = if (random.nextBoolean()) 1 else 0

  val idCounter = new AtomicLong()
  val tickCounter = new AtomicLong()
  val random = Random

  def randomInt(maxInclusive:Int) = random.nextInt(maxInclusive)
  def randomDirection = Direction.byIndex(random.nextInt(4))
  //TODO format with zeros
  def makeId(prefix:String = "") = prefix + idCounter.incrementAndGet().toString
  def nextTickNumber = tickCounter.getAndIncrement()
}
