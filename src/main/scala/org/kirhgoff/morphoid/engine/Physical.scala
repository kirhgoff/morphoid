package org.kirhgoff.morphoid.engine

/**
  * Physical point in space, now represent
  * cell location in cellular automata
  * TODO extract automata properties,
  * i.e. infinite space rings
  * Created by <a href="mailto:kirill.lastovirya@gmail.com">kirhgoff</a> on 2/9/17.
  */
class Physical(var x:Int, var y:Int) {

  def this(physical: Physical) = this(physical.x, physical.y)

  def move(direction: Direction) = {
    x = x + direction.dx
    y = y + direction.dy
    this
  }

  def moveTo(x:Int, y:Int) = {
    this.x = x
    this.y = y
    this
  }

  def nextTo(direction: Direction) = {
    new Physical(x + direction.dx, y + direction.dy)
  }

  override def toString: String = s"[$x, $y]"
  override def hashCode = toString.hashCode
  override def equals(other:Any) = other match {
    case x:Physical if x != null => toString.equals(x.toString)
    case _ => false
  }

  def copy = new Physical(this)

}

object Physical {
  def apply(x:Int, y:Int) = new Physical(x, y)
}
