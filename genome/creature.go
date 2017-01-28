package genome

// Creature : live being
type Creature struct {
	Kind  string
	cells []Limb
}

// NewCreature creates Creature with given parameters
func NewCreature(kind string, limbs []Limb) Creature {
	return Creature{kind, limbs}
}
