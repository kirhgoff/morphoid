package main

import tl "github.com/JoelOtter/termloop"

// Projectile : moving object
type Projectile struct {
	*tl.Entity
	direction    Direction
	energy       int
	speed        int
	currentSpeed int
	level        *tl.BaseLevel
}

// NewProjectile : create new projectile
func NewProjectile(level *tl.BaseLevel, x, y int, direction Direction) Projectile {
	dx, dy := direction.shifts()
	projectile := Projectile{
		Entity:       tl.NewEntity(x+dx, y+dy, 1, 1),
		direction:    direction,
		energy:       20,
		speed:        700,
		currentSpeed: 700,
		level:        level,
	}
	// Set the character at position (0, 0) on the entity.
	character := projectile.getSymbol()
	projectile.SetCell(0, 0, &tl.Cell{Fg: tl.ColorRed | tl.AttrBold, Ch: character})
	return projectile
}

func (projectile *Projectile) getSymbol() rune {
	if projectile.direction == LEFT || projectile.direction == RIGHT {
		return '-'
	}
	return '|'
}

// Draw : decreases the energy, moves the projectile
// and destroys it if energy is over
func (projectile *Projectile) Draw(screen *tl.Screen) {
	if projectile.energy < 0 {
		projectile.level.RemoveEntity(projectile)
		return
	} else if projectile.energy == 0 {
		projectile.SetCell(0, 0, &tl.Cell{Fg: tl.ColorRed | tl.AttrBold, Ch: '*'})
	}

	if projectile.currentSpeed < 0 {
		projectile.currentSpeed = projectile.speed
	} else if projectile.currentSpeed == 0 {
		x, y := projectile.Position()
		dx, dy := projectile.direction.shifts()
		projectile.SetPosition(x+dx, y+dy)
		projectile.energy--
	}
	projectile.currentSpeed--
	projectile.Entity.Draw(screen)
}

// Collide : destroyed on collision
func (projectile *Projectile) Collide(physical tl.Physical) {
	projectile.energy = -1
}
