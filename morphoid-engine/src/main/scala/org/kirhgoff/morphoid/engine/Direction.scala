package org.kirhgoff.morphoid.engine

/**
  * Created by <a href="mailto:kirill.lastovirya@gmail.com">kirhgoff</a> on 2/9/17.
  */

//     ______________
//    | (0,0)      (1,0)            North
//    |                         West    East
//    | (0,1)                       South

abstract class Direction(val dx:Int, val dy:Int)
case object North extends Direction(0, -1)
case object East extends Direction(1, 0)
case object South extends Direction(0, 1)
case object West extends Direction(-1, 0)

object Direction {
  def clockwise = Array(North, East, South, West)
  def all = clockwise
  def byIndex(i:Int) = clockwise(i)

  def byDelta(dx:Int, dy:Int):Direction =
    if (dx >= 0 && dy >= 0)
      if (dx >= dy) East else South
    else if (dx >= 0 && dy <= 0)
      if (dx >= -dy) East else North
    else if (dx <= 0 && dy >= 0)
      if (dx <= dy) West else South
    else { //if (dx <= 0 && dy <= 0)
      if (dx <= dy) West else North
    }
}
