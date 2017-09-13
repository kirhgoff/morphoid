package org.kirhgoff.morphoid.engine

/**
  * Created by <a href="mailto:kirill.lastovirya@gmail.com">kirhgoff</a> on 10/9/17.
  */
case class Rect(topLeft:Cell, bottomRight:Cell) {
  def x1 = topLeft.x
  def y1 = topLeft.y
  def x2 = bottomRight.x
  def y2 = bottomRight.y

  def topRight = Cell(x1, y2)
  def bottomLeft = Cell(x2, y1)

  def includes(rect: Rect) =
    x1 <= rect.x1 && y1 <= rect.y1 && x2 >= rect.x2 && y2 >= rect.y2

  def intersects(rect: Rect): Boolean =
    inside(rect.x1, rect.y1) ||
    inside(rect.x1, rect.y2) ||
    inside(rect.x2, rect.y1) ||
    inside(rect.x2, rect.y2)

  def inside(x:Int, y:Int) = x1 <= x && x <= x2 && y1 <= y && y <= y2

  def move(direction: Direction) = Rect(
    x1 + direction.dx,
    y1 + direction.dy,
    x2 + direction.dx,
    y2 + direction.dy
  )


}

object Rect {
  def apply (left:Int, top:Int, right:Int, bottom:Int) =
    new Rect(Cell(left, top), Cell(right, bottom))
}
