package main

import "fmt"

// Direction : int for now
type Direction int

// Direction constants
const (
	RIGHT Direction = 1 + iota
	LEFT
	UP
	DOWN
)

func (d Direction) shifts() (int, int) {
	switch d {
	case RIGHT:
		return 1, 0
	case LEFT:
		return -1, 0
	case UP:
		return 0, -1
	case DOWN:
		return 0, 1
	}
	panic(fmt.Sprintf("illegal direction %v", d))
}
