package org.kirhgoff.munchkin

abstract class Direction
case object North extends Direction
case object East extends Direction
case object South extends Direction
case object West extends Direction

object Direction {
  def clockwise = List(North, East, South, West)
  def all = clockwise
}

