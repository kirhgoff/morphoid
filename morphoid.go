package main

import (
	"fmt"

	tl "github.com/JoelOtter/termloop"
)

var (
	logChan = make(chan string)
)

func log(format string, args ...interface{}) {
	logChan <- fmt.Sprintf(format, args...)
}

func runLoggingChannel(game *tl.Game) {
	for {
		game.Log(<-logChan)
	}
}

func main() {
	game := tl.NewGame()
	level := tl.NewBaseLevel(tl.Cell{
		Bg: tl.ColorBlack,
		Fg: tl.ColorBlack,
		Ch: ' ',
	})
	//level.AddEntity(tl.NewRectangle(10, 10, 50, 20, tl.ColorBlue))
	player := Player{
		Entity: tl.NewEntity(1, 1, 1, 1),
		level:  level,
	}
	// Set the character at position (0, 0) on the entity.
	// TODO incapsulate cell creation
	player.SetCell(0, 0, &tl.Cell{Fg: tl.ColorGreen | tl.AttrBold, Ch: '@'})
	level.AddEntity(&player)
	game.Screen().SetLevel(level)
	game.SetDebugOn(true)
	go runLoggingChannel(game)
	game.Log("Morhoid 2.0 started")
	game.Start()
}
