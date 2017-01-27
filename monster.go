package main

import (
	"math/rand"
	"time"

	tl "github.com/JoelOtter/termloop"
)

// Monster : creature in the world
type Monster struct {
	*tl.Entity
	level  *tl.BaseLevel
	energy int
	speed  int
}

// NewMonster : creates new instance of monster
func NewMonster(level *tl.BaseLevel, x, y, energy, speed int) Monster {
	monster := Monster{
		Entity: tl.NewEntity(x, y, 1, 1),
		level:  level,
		energy: energy,
		speed:  speed,
	}
	cell := tl.Cell{Fg: monster.getColor(), Ch: monster.getRune()}
	monster.SetCell(0, 0, &cell)
	return monster
}

func (monster *Monster) getRune() rune {
	return rune(48 + monster.energy)
}

func (monster *Monster) getColor() tl.Attr {
	return tl.ColorGreen
}

func randShifts() (int, int) {
	return rand.Intn(2) - 1, rand.Intn(2) - 1
}

// Tick decrases the energy, moves the projectile
// and destroys it if energy is over
func (monster *Monster) run() {
	for {
		time.Sleep(time.Millisecond * time.Duration(monster.speed))
		x, y := monster.Position()
		if monster.energy <= 0 {
			monster.level.RemoveEntity(monster)
			corpse := NewCorpse(monster.level, x, y)
			monster.level.AddEntity(&corpse)
			return
		}
		//TODO show damage
		//monster.SetCell(0, 0, &tl.Cell{Fg: tl.ColorRed | tl.AttrBold, Ch: '*'})
		dx, dy := randShifts()
		monster.SetPosition(x+dx, y+dy)
	}
}
