package engine

// World map of the world
type World interface {
	GetEntities() []Entity
	GetCreatures() []Creature
	GetLore(creature Creature) Lore
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
func (world *BasicWorld) GetLore(creature Creature) Lore {
	id := creature.GetID()
	radius := creature.GetVision()

	rx := creature.GetX() - radius
	ry := creature.GetY() - radius
	rw := 2*radius + creature.GetWidth()
	rh := 2*radius + creature.GetHeight()

	mom := make(map[int]map[int]string)

	for _, entity := range world.GetEntities() {
		if entity.Intersects(rx, ry, rw, rh) {
			id = entity.GetID()
			for i := entity.GetX(); i < entity.GetX()+entity.GetWidth(); i++ {
				if _, present := mom[i]; !present {
					mom[i] = make(map[int]string)
				}
				for j := entity.GetY(); j < entity.GetY()+entity.GetHeight(); j++ {
					mom[i][j] = id
				}
			}
		}
	}

	for i := rx; i < rx+rw; i++ {
		if i < creature.GetX() || i > creature.GetX()+creature.GetWidth() {
			for j := ry; j < ry+rh; j++ {
				if j < creature.GetY() || j > creature.GetY()+creature.GetHeight() {

				}
			}
		}
	}
	//	for i := x - radius; i < x + radius;
	return nil //TODO
}
