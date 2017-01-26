package main

import (
	tl "github.com/JoelOtter/termloop"
)

// Player : user-controlled entity
type Player struct {
	*tl.Entity
	prevX int
	prevY int
	level *tl.BaseLevel
}

func (player *Player) shoot(direction Direction) {
	//log("shoot %v", direction)
	x, y := player.Position()
	dx, dy := direction.shifts()
	projectile := Projectile{
		Entity:    tl.NewEntity(x+dx, y+dy, 1, 1),
		direction: direction,
		energy:    20,
		level:     player.level,
	}
	// Set the character at position (0, 0) on the entity.
	character := projectile.getSymbol()
	projectile.SetCell(0, 0, &tl.Cell{Fg: tl.ColorRed | tl.AttrBold, Ch: character})
	player.level.AddEntity(&projectile)
	go projectile.run()
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
