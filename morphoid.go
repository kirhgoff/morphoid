package main

import (
	"fmt"
	"math/rand"
	"time"

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
	rand.Seed(time.Now().UTC().UnixNano())
	game := tl.NewGame()
	level := tl.NewBaseLevel(tl.Cell{
		Bg: tl.ColorBlack,
		Fg: tl.ColorBlack,
		Ch: ' ',
	})
	//width, height := game.Screen().Size()
	//fmt.Printf("Screen size: w=%v,h=%v\n", width, height)
	// Create player
	player := NewPlayer(level, 10, 10)
	level.AddEntity(&player)

	//Create monsters
	for i := 0; i < 20; i++ {
		x := rand.Intn(100)
		y := rand.Intn(30)
		energy := rand.Intn(10)
		speed := rand.Intn(3000) + 2000
		monster := NewMonster(level, x, y, energy, speed)
		level.AddEntity(&monster)
	}

	game.Screen().SetLevel(level)
	game.SetDebugOn(true)
	go runLoggingChannel(game)
	log("Morhoid 2.0 started")
	game.Start()
}
