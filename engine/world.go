package engine

// World map of the world
type World interface {
	GetEntities() []Entity
	GetCreatures() []Creature
	GetLore(creature *Creature) Lore
}

// BasicWorld implementation
type BasicWorld struct {
	Entities  []Entity
	Creatures []Creature //TODO should be references
}

// NewWorld creates world
func NewWorld(entities []Entity) World {
	creatures := make([]Creature, 0)
	for _, entity := range entities {
		if creature, ok := entity.(Creature); ok {
			creatures = append(creatures, creature)
		}
	}
	return &BasicWorld{entities, creatures}
}

// GetEntities get all objects in the world
func (world *BasicWorld) GetEntities() []Entity {
	return world.Entities
}

// GetCreatures get living beings
func (world *BasicWorld) GetCreatures() []Creature {
	return world.Creatures
}

// GetLore returns surroundings for a creature
func (world *BasicWorld) GetLore(creature *Creature) Lore {
	return nil //TODO
}
