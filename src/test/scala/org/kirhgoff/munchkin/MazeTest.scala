package org.kirhgoff.munchkin

import org.scalatest._

class MazeTest extends FlatSpec with Matchers {

  "Maze" should "not allow initially incorrect mazes" in {
    //Incorrect size
    an [IllegalArgumentException] should be thrownBy
      new Maze(Array(Array(Room(South)), Array(Room(North), Room(North))))

    //Non-closed from outside
    an [IllegalArgumentException] should be thrownBy
      new Maze(Array(Array(Plain())))

    //Incorrect walls
    an [IllegalArgumentException] should be thrownBy
      new Maze(Array(Array(Room(East), Plain())))
  }

  it should "able to map positions to cells" in {
    val maze = new Maze(Array(
      Array(Room(East), Room(South)),
      Array(Room(North), Room(West))
    ))

    maze.cellFor(Position(0,0)).walls() should not contain()
  }

  it should "provide next available step" in {
    new Maze(Array(Array(Cell(List(East, South, West, North)))))
      .wander(Position(0, 0)) shouldBe empty

    new Maze(Array(Array(Room(East), Room(West))))
      .wander(Position(0,0)) shouldBe List(East)
  }

  it should "be able to build a path in simple" in {
    //3x1
    val maze = new Maze(Array(
      Array(Room(East), Cell(List(North, South)), Room(West))
    ))
    val path = maze.path(Position(0, 0), Position(2, 0))
    path should contain allOf (East, East)
  }

  it should "be able to build a path" in {
    //3x1
    val maze = new Maze(Array(
      Array(Room(East), Cell(List(North, South)), Room(West))
    ))
    val path = maze.path(Position(0, 0), Position(2, 0))
    path should contain allOf (East, East)
  }

  it should "find a way from normal maze" in {
    //Simple 2x2 map
    val maze = new Maze(Array(
      Array(Room(South), Room(South)),
      Array(Cell(List(West, South)), Cell(List(South, East)))
    ))

    val path = maze.path(Position(0, 0), Position(1, 0))
    path should contain allOf (South, East, North)
  }
}