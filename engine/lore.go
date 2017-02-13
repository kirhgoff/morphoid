package engine

// Lore : information about surroundings
type Lore interface {
	GetID() string
	GetX() int
	GetY() int
	Radius() int
	// TODO make it neuron
	Reduce(kind string) float64
}

// BasicLore is implementation of basic storage of data per cell
type BasicLore struct {
	id     string
	x      int
	y      int
	radius int
	cells  []map[string]float64
}

// NewLore creates new lore with required parameters for chosen world
func NewLore(id string, x, y, radius int, cells []map[string]float64) *BasicLore {
	return &BasicLore{id, x, y, radius, cells}
}

// GetID returns ID of creature for which lore was created
func (lore *BasicLore) GetID() string { return lore.id }

// GetX returns x of basic lore center
func (lore *BasicLore) GetX() int { return lore.x }

// GetY returns y of basic lore center
func (lore *BasicLore) GetY() int { return lore.y }

// GetRadius returns radius of basic lore
func (lore *BasicLore) GetRadius() int { return lore.radius }

// Reduce simply accumulates all the cell values of required kind
func (lore *BasicLore) Reduce(kind string) float64 {
	acc := 0.0
	for _, cell := range lore.cells {
		acc += cell[kind]
	}
	return acc
}
