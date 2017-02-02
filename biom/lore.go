package biom

// Lore : information about surroundings
type Lore interface {
	Radius() int
	GetProperty(kind string, dx, dy int) interface{}
}
