package main

import (
	tl "github.com/JoelOtter/termloop"
)

// Player : user-controlled entity
type Player struct {
	*tl.Entity
	prevX  int
	prevY  int
	level  *tl.BaseLevel
	energy int
}

// NewPlayer : create new player instance
func NewPlayer(level *tl.BaseLevel, x, y int) Player {
	player := Player{
		Entity: tl.NewEntity(x, y, 1, 1),
		level:  level,
		energy: 1,
	}
	cell := tl.Cell{Fg: tl.ColorYellow | tl.AttrBold, Ch: '@'}
	player.SetCell(0, 0, &cell)
	return player
}

func (player *Player) shoot(direction Direction) {
	x, y := player.Position()
	projectile := NewProjectile(player.level, x, y, direction)
	player.level.AddEntity(&projectile)
}

// Draw : draw player
func (player *Player) Draw(screen *tl.Screen) {
	if player.energy <= 0 {
		x, y := player.Position()
		corpse := NewCorpse(player.level, x, y)
		player.level.RemoveEntity(player)
		player.level.AddEntity(&corpse)
	}
	player.Entity.Draw(screen)
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

// Collide : any collision kills player
func (player *Player) Collide(collision tl.Physical) {
	if monster, ok := collision.(*Monster); ok {
		player.energy -= monster.energy
	}
}
