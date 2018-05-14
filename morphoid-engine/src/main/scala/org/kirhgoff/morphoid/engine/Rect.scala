package org.kirhgoff.morphoid.engine

/**
  * Created by <a href="mailto:kirill.lastovirya@gmail.com">kirhgoff</a> on 10/9/17.
  */
case class Rect(x1:Int, y1:Int, x2:Int, y2:Int) {
  def topRight = new Physical(x1, y2)
  def bottomLeft = new Physical(x2, y1)

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

  def decompose = for (x <- x1 to x2; y <- y1 to y2) yield new Physical(x, y)


  def touches(rect: Rect, direction: Direction) : Boolean = {
    def between(a:Int, a1:Int, a2:Int) = a1 <= a && a <= a2
    // TODO simplify
    def verticallyAligned =
      between(y1, rect.y1, rect.y2) ||
      between(y2, rect.y1, rect.y2) ||
      between(rect.y1, y1, y2) ||
      between(rect.y2, y1, y2)

    def horizontallyAligned =
      between(x1, rect.x1, rect.x2) ||
      between(x2, rect.x1, rect.x2) ||
      between(rect.x1, x1, x2) ||
      between(rect.x2, x1, x2)

    if (rect.includes(this) || this.includes(rect)) {
      true
    } else if (rect.intersects(this)) {
      if (direction == North) {
        rect.y1 < y1
      } else if (direction == South) {
        rect.y2 > y2
      } else if (direction == West) {
        rect.x2 > x2
      } else if (direction == East) {
        rect.x1 < x1
      } else {
        throw new IllegalStateException(s"Impossible intersection: this=$this rect=$rect direction=$direction")
      }
    } else if (direction == West) {
      x2 + 1 == rect.x1 && verticallyAligned
    } else if (direction == East) {
      x1 - 1 == rect.x2 && verticallyAligned
    } else if (direction == North) {
      y1 - 1 == rect.y2 && horizontallyAligned
    } else if (direction == South) {
      y2 + 1 == rect.y1 && horizontallyAligned
    } else {
      false
    }
  }
}

object Rect {
  def apply(topLeft:Physical, bottomRight:Physical) =
    new Rect(topLeft.x, topLeft.y, bottomRight.x, bottomRight.y)

  def inflate(c:Physical, d:Int) = Rect(c.x - d, c.y - d, c.x + d, c.y + d)
}
