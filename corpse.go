package main

import tl "github.com/JoelOtter/termloop"

// Corpse : creature in the world
type Corpse struct {
	*tl.Entity
	level *tl.BaseLevel
}

func (corpse *Corpse) getColor() tl.Attr {
	return tl.ColorWhite
}

// NewCorpse : creates new corpse and adds it to level
func NewCorpse(level *tl.BaseLevel, x, y int) Corpse {
	corpse := Corpse{
		Entity: tl.NewEntity(x, y, 1, 1),
		level:  level,
	}
	cell := tl.Cell{Fg: corpse.getColor(), Ch: 'X'}
	corpse.SetCell(0, 0, &cell)
	return corpse
}
