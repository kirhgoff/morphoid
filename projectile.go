package main

import (
	"time"

	tl "github.com/JoelOtter/termloop"
)

// Projectile : moving object
type Projectile struct {
	*tl.Entity
	direction Direction
	energy    int16
	level     *tl.BaseLevel
}

func (projectile *Projectile) getSymbol() rune {
	if projectile.direction == LEFT || projectile.direction == RIGHT {
		return '-'
	}
	return '|'
}

// Tick decrases the energy, moves the projectile
// and destroys it if energy is over
func (projectile *Projectile) run() {
	for {
		time.Sleep(time.Millisecond * 50)
		if projectile.energy < 0 {
			projectile.level.RemoveEntity(projectile)
			return
		} else if projectile.energy == 0 {
			projectile.SetCell(0, 0, &tl.Cell{Fg: tl.ColorRed | tl.AttrBold, Ch: '*'})
		}
		x, y := projectile.Position()
		dx, dy := projectile.direction.shifts()
		projectile.SetPosition(x+dx, y+dy)
		projectile.energy--
	}
}
