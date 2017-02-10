package engine

// Lore : information about surroundings
type Lore interface {
	GetX() int
	GetY() int
	Radius() int
	Reduce(kind string) int
}
