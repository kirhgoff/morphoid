package org.kirhgoff.morphoid.engine

/**
  * Created by <a href="mailto:kirill.lastovirya@gmail.com">kirhgoff</a> on 10/9/17.
  */
case class Rect(x1:Int, y1:Int, x2:Int, y2:Int) {

  def topRight = Physical(x1, y2)
  def bottomLeft = Physical(x2, y1)

  def includes(rect: Rect) =
    x1 <= rect.x1 && y1 <= rect.y1 && x2 >= rect.x2 && y2 >= rect.y2

  def intersects(rect: Rect): Boolean =
    inside(rect.x1, rect.y1) ||
    inside(rect.x1, rect.y2) ||
    inside(rect.x2, rect.y1) ||
    inside(rect.x2, rect.y2)

  def inside(x:Int, y:Int) = x1 <= x && x <= x2 && y1 <= y && y <= y2

  def move(direction: Direction) = new Rect(
    x1 + direction.dx,
    y1 + direction.dy,
    x2 + direction.dx,
    y2 + direction.dy
  )

  def inflate(d: Int) = Rect(x1 - d, y1 - d, x2 + d, y2 + d)

  def decompose = for (x <- x1 to x2; y <- y1 to y2) yield Physical(x, y)
}

object Rect {
  def apply(topLeft:Physical, bottomRight:Physical) =
    new Rect(topLeft.x, topLeft.y, bottomRight.x, bottomRight.y)

  def inflate(c:Physical, d:Int) = Rect(c.x - d, c.y - d, c.x + d, c.y + d)
}
