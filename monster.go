package main

import (
	"math/rand"

	tl "github.com/JoelOtter/termloop"
)

// Monster : creature in the world
type Monster struct {
	*tl.Entity
	level        *tl.BaseLevel
	energy       int
	speed        int
	currentSpeed int
}

// NewMonster : creates new instance of monster
func NewMonster(level *tl.BaseLevel, x, y, energy, speed int) Monster {
	monster := Monster{
		Entity:       tl.NewEntity(x, y, 1, 1),
		level:        level,
		energy:       energy,
		speed:        speed, //TODO rename to steps
		currentSpeed: speed,
	}
	cell := tl.Cell{Fg: monster.getColor(), Ch: monster.getRune()}
	monster.SetCell(0, 0, &cell)
	return monster
}

func (monster *Monster) getRune() rune {
	return rune(48 + monster.energy) //ASCII code of zero
}

func (monster *Monster) getColor() tl.Attr {
	return tl.ColorGreen
}

func randShifts() (int, int) {
	return rand.Intn(3) - 1, rand.Intn(3) - 1
}

// Draw : and destroys it if energy is over
func (monster *Monster) Draw(screen *tl.Screen) {
	x, y := monster.Position()
	if monster.energy <= 0 {
		monster.level.RemoveEntity(monster)
		corpse := NewCorpse(monster.level, x, y)
		monster.level.AddEntity(&corpse)
		return
	}
	// TODO redo using channels
	if monster.currentSpeed < 0 {
		monster.currentSpeed = monster.speed
	} else if monster.currentSpeed == 0 {
		width, height := screen.Size()
		dx, dy := randShifts()
		newX := x + dx
		newY := y + dy
		if newX < 0 || newX > width-1 {
			newX = x - dx
		}
		if newY < 0 || newY > height-1 {
			newY = y - dy
		}
		changeCharacter(monster.Entity, tl.ColorGreen|tl.AttrBold, monster.getRune())
		monster.SetPosition(newX, newY)
	}

	monster.currentSpeed--
	monster.Entity.Draw(screen)
}

// Collide : monster should eat other monster
// eat another weaker monster and decrease energy from bullet
func (monster *Monster) Collide(collision tl.Physical) {
	log("Monster collide %+v", collision)
	if projectile, ok := collision.(*Projectile); ok {
		monster.energy--
		changeCharacter(monster.Entity, tl.ColorRed|tl.AttrBold, '*')
		if monster.energy > 0 {
			x, y := monster.Position()
			dx, dy := projectile.direction.shifts()
			monster.SetPosition(x+dx, y+dy)
		}
	} else if otherMonster, ok := collision.(*Monster); ok {
		//TODO think how to make it better
		if otherMonster.energy > monster.energy {
			otherMonster.energy += monster.energy
			monster.energy = 0
		} else {
			monster.energy += otherMonster.energy
			otherMonster.energy = 0
		}
	}
}

func changeCharacter(entity *tl.Entity, color tl.Attr, character rune) {
	entity.SetCell(0, 0, &tl.Cell{Fg: color, Ch: character})
}
