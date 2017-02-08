package engine

import (
	"fmt"

	uuid "github.com/nu7hatch/gouuid"
)

// Creature : live being
type Creature interface {
	GetID() string
	GetKind() string
	GetLimbs() []Limb
	GetReceptors() []Receptor
	Process(Lore) []Action
}

// Process applies surroundings to creature and gets
// its reactions
func (creature *Creature) Process(lore Lore) []Action {
	actions := make([]Action, 0)
	for _, receptor := range creature.Receptors {
		signals := receptor.Process(lore)
		for _, signal := range signals {
			action, ok := signal.(Action)
			if !ok {
				panic(fmt.Errorf("Cannot convert %+v signal to action", action))
			}
			action.CreatureID = creature.CreatureID
			actions = append(actions, action)
		}
	}
	return actions
}

// NewCreature creates Creature with given parameters
func NewCreature(kind string, limbs []Limb) Creature {
	// We will have at least one receptor and one actor
	receptors := make([]Receptor, 0)
	for _, limb := range limbs {
		// Collect receptors
		if receptor, ok := limb.(Receptor); ok {
			receptors = append(receptors, receptor)
		}
	}
	uuid, err := uuid.NewV4()
	if err != nil {
		panic(err)
	}
	return Creature{uuid.String(), kind, limbs, receptors}
}
