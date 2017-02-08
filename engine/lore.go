package engine

// Lore : information about surroundings
type Lore struct {
	Radius() int
	GetProperty(kind string, dx, dy int) interface{}
	MapAround(kind string) []interface{}
}
