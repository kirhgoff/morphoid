package genome

// Creature : live being
type Creature struct {
	Kind      string
	cells     []Limb
	receptors []Receptor
	actors    []Actor
}

// NewCreature creates Creature with given parameters
func NewCreature(kind string, limbs []Limb) Creature {
	// We will have at least one receptor and one actor
	receptors := make([]Receptor, 1)
	actors := make([]Actor, 1)
	for _, limb := range limbs {

		// Collect receptors
		receptor, ok := limb.(Receptor)
		if ok {
			receptors = append(receptors, receptor)
		}
		// Collect actors
		actor, ok := limb.(Actor)
		if ok {
			actors = append(actors, actor)
		}
	}
	return Creature{kind, limbs, receptors, actors}
}
