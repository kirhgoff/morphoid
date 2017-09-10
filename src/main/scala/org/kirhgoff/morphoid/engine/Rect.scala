package org.kirhgoff.morphoid.engine

/**
  * Created by <a href="mailto:kirill.lastovirya@gmail.com">kirhgoff</a> on 10/9/17.
  */
case class Rect(topLeft:Cell, bottomRight:Cell) {
  def x1 = topLeft.x
  def y1 = topLeft.y
  def x2 = bottomRight.x
  def y2 = bottomRight.y

  def includes(rect: Rect) =
    x1 <= rect.x1 && y1 <= rect.y1 && x2 >= rect.x2 && y2 >= rect.y2


}

object Rect {
  def apply (left:Int, top:Int, right:Int, bottom:Int) =
    new Rect(Cell(left, top), Cell(right, bottom))
}
