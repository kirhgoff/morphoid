package engine

import "fmt"

// Creature : live being
type Creature interface {
	Entity
	GetLimbs() []Limb
	GetReceptors() []Receptor
	Process(Lore) []Action
	GetVision() int
}

// BaseCreature implementation
type BaseCreature struct {
	*BaseEntity
	Limbs     []Limb
	Receptors []Receptor
}

// NewCreature creates Creature with given parameters
func NewCreature(kind string, x, y int, limbs []Limb) *BaseCreature {
	// We will have at least one receptor and one actor
	receptors := make([]Receptor, 0)
	for _, limb := range limbs {
		// Collect receptors
		if receptor, ok := limb.(Receptor); ok {
			receptors = append(receptors, receptor)
		}
	}

	return &BaseCreature{NewEntity(kind, x, y, 0, 0), limbs, receptors}
}

// GetLimbs implementation
func (creature *BaseCreature) GetLimbs() []Limb {
	return creature.Limbs
}

// GetReceptors implementation
func (creature *BaseCreature) GetReceptors() []Receptor {
	return creature.Receptors
}

// Process applies surroundings to creature and gets
// its reactions
func (creature *BaseCreature) Process(lore Lore) []Action {
	actions := make([]Action, 0)
	for _, receptor := range creature.Receptors {
		signals := receptor.Process(lore)
		for _, signal := range signals {
			action, ok := signal.(Action)
			if !ok {
				panic(fmt.Errorf("Cannot convert %+v signal to action", action))
			}
			action.CreatureID = creature.GetID()
			actions = append(actions, action)
		}
	}
	return actions
}

// GetVision returns number of cells visible by the receptor
func (creature *BaseCreature) GetVision() int {
	vision := 0
	for _, receptor := range creature.Receptors {
		if vision < receptor.GetVision() {
			vision = receptor.GetVision()
		}
	}
	return vision
}
