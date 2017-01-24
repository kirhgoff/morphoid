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

// Player : user-controlled entity
type Player struct {
	*tl.Entity
	prevX int
	prevY int
	level *tl.BaseLevel
}

// Projectile : moving object
type Projectile struct {
	*tl.Entity
	direction Direction
}

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

// Draw : makes camera move
/*func (player *Player) Draw(screen *tl.Screen) {
	screenWidth, screenHeight := screen.Size()
	x, y := player.Position()
	// TODO set offset only when user is close to border
	player.level.SetOffset(screenWidth/2-x, screenHeight/2-y)
	player.Entity.Draw(screen)
}*/

func (player *Player) shoot(direction Direction) {
	log("shoot %v", direction)
	x, y := player.Position()
	dx, dy := direction.shifts()
	projectile := Projectile{
		Entity:    tl.NewEntity(x+dx, y+dy, 1, 1),
		direction: direction,
	}
	// Set the character at position (0, 0) on the entity.
	projectile.SetCell(0, 0, &tl.Cell{Fg: tl.ColorRed | tl.AttrBold, Ch: '*'})
	player.level.AddEntity(&projectile)
	//player.Entity.Draw(screen)
}

// Tick : standard function implementation
func (player *Player) Tick(event tl.Event) {
	if event.Type == tl.EventKey { // Is it a keyboard event?
		player.prevX, player.prevY = player.Position()
		switch event.Key { // If so, switch on the pressed key.
		case tl.KeyArrowRight:
			player.shoot(RIGHT)
		case tl.KeyArrowLeft:
			player.shoot(LEFT)
		case tl.KeyArrowUp:
			player.shoot(UP)
		case tl.KeyArrowDown:
			player.shoot(DOWN)
		default:
			// TODO create keybord configuration
			switch event.Ch {
			case 'w', 'W':
				player.SetPosition(player.prevX, player.prevY-1)
			case 's', 'S':
				player.SetPosition(player.prevX, player.prevY+1)
			case 'a', 'A':
				player.SetPosition(player.prevX-1, player.prevY)
			case 'd', 'D':
				player.SetPosition(player.prevX+1, player.prevY)
			}
		}
	}
}

// Collide : standard collision
func (player *Player) Collide(collision tl.Physical) {
	// Check if it's a Rectangle we're colliding with
	if _, ok := collision.(*tl.Rectangle); ok {
		player.SetPosition(player.prevX, player.prevY)
	}
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
	player.SetCell(0, 0, &tl.Cell{Fg: tl.ColorGreen | tl.AttrBold, Ch: '@'})
	level.AddEntity(&player)
	game.Screen().SetLevel(level)
	game.SetDebugOn(true)
	go runLoggingChannel(game)
	game.Log("Morhoid 2.0 started")
	game.Start()
}
