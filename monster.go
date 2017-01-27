package main

import (
	"math/rand"
	"time"

	tl "github.com/JoelOtter/termloop"
)

// Monster : creature in the world
type Monster struct {
	*tl.Entity
	level        *tl.BaseLevel
	energy       int
	speed        int
	takingDamage bool
}

// NewMonster : creates new instance of monster
func NewMonster(level *tl.BaseLevel, x, y, energy, speed int) Monster {
	monster := Monster{
		Entity:       tl.NewEntity(x, y, 1, 1),
		level:        level,
		energy:       energy,
		speed:        speed,
		takingDamage: false,
	}
	cell := tl.Cell{Fg: monster.getColor(), Ch: monster.getRune()}
	monster.SetCell(0, 0, &cell)
	return monster
}

//func (monster *Monster) Draw(screen *tl.Screen) {
//player.Entity.Draw(screen)
//}

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
		//newX := x + dx
		//newY := y + dy

		monster.SetPosition(x+dx, y+dy)
	}
}

// Collide : monster should eat other monster
// eat another weaker monster and decrease energy from bullet
func (monster *Monster) Collide(collision tl.Physical) {
	//log("Monster collide %+v", physical)
	if projectile, ok := collision.(*Projectile); ok {
		monster.energy--
		x, y := monster.Position()
		dx, dy := projectile.direction.shifts()
		monster.SetPosition(x+dx, y+dy)
	} else if otherMonster, ok := collision.(*Monster); ok {
		if otherMonster.energy > monster.energy {
			monster.level.RemoveEntity(monster)
		} else {
			otherMonster.energy += monster.energy
		}
	}
}
