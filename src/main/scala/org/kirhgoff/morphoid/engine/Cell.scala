package org.kirhgoff.morphoid.engine

/**
  * Created by <a href="mailto:kirill.lastovirya@gmail.com">kirhgoff</a> on 2/9/17.
  */
case class Cell(x:Int, y:Int) {
  override def toString: String = s"[$x, $y]"

  def nextTo(direction: Direction) = Cell(x + direction.dx, y + direction.dy)
}
