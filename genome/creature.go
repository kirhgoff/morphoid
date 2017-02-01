package genome

import (
	"fmt"

	biom "github.com/kirhgoff/morphoid/biom"
	uuid "github.com/nu7hatch/gouuid"
)

// Creature : live being
type Creature struct {
	CreatureID string
	Kind       string
	cells      []Limb
	receptors  []Receptor
	actors     []Actor
}

// Process applies surroundings to creature and gets
// its reactions
func (creature *Creature) Process(lore *biom.Lore) []Action {
	actions := make([]Action, 0)
	for _, receptor := range creature.receptors {
		signals := receptor.Process(lore)
		for _, signal := range signals {
			action, ok := signal.(Action)
			if !ok {
				panic(fmt.Errorf("Cannot convert %+v signal to action", action))
			}
			actions = append(actions, signal)
		}
	}
	return actions
}

// NewCreature creates Creature with given parameters
func NewCreature(kind string, limbs []Limb) Creature {
	// We will have at least one receptor and one actor
	receptors := make([]Receptor, 0)
	actors := make([]Actor, 0)
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
	uuid, err := uuid.NewV4()
	if err != nil {
		panic(err)
	}
	return Creature{uuid.String(), kind, limbs, receptors, actors}
}
