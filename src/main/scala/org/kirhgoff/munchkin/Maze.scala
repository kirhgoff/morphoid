package org.kirhgoff.munchkin

import scala.collection.mutable

trait MazeCell {
  def walls():List[Direction]
  def hasWallAt(direction: Direction):Boolean
}

//Could go to all 4 sides
case class Plain() extends MazeCell {
  override def hasWallAt(direction: Direction) = false
  override def walls(): List[Direction] = Direction.all
}
//Only one entrance
case class Room(entrance:Direction) extends MazeCell {
  override def hasWallAt(direction: Direction) = direction != entrance
  override def walls(): List[Direction] = Direction.all.diff(List(entrance))
}
//Custom
case class Cell(walls:List[Direction]) extends MazeCell {
  override def hasWallAt(direction: Direction) = walls.contains(direction)
}

case class Position(x:Int, y:Int)

//columns[rows]
class Maze(cells: Array[Array[MazeCell]]) {
  verify (cells)

  def verify(cells: Array[Array[MazeCell]]) = {
    //Check array size
    if (cells.map(_.length).distinct.length != 1)
      throw new IllegalArgumentException("Different length of rows!")

    val width = cells.length
    val height = cells(0).length

    //Check surrounding walls
    surroundedFrom(cells(0), West)
    surroundedFrom(cells(width - 1), East)
    surroundedFrom(cells.map(_(0)), North)
    surroundedFrom(cells.map(_(height - 1)), South)
  }


  private def surroundedFrom(cells: Array[MazeCell], direction: Direction) = {
    if (cells.exists(!_.hasWallAt(direction)))
      throw new IllegalArgumentException(s"Empty cell on $direction")
  }

  def path (from:Position, to:Position):List[Direction] = {
    val visited = mutable.MutableList()
    List()
  }

  def cellFor(from: Position):MazeCell = ???

  def wander(from:Position):Position = {
    val cell = cellFor(from)
  }
}


