package org.kirhgoff.morphoid.engine

/**
  * Physical point in space, now represent
  * cell location in cellular automata
  * TODO extract automata properties,
  * i.e. infinite space rings
  * Created by <a href="mailto:kirill.lastovirya@gmail.com">kirhgoff</a> on 2/9/17.
  */
case class Physical(x:Int, y:Int) {
  override def toString: String = s"[$x, $y]"

  def nextTo(direction: Direction) = new Physical(x + direction.dx, y + direction.dy)

}
