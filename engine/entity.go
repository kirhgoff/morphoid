package engine

// Entity represents something in game
type Entity interface {
	GetID() string
	GetKind() string
	GetX() int
	GetY() int
	GetWidth() int
	GetHeight() int
}

// BaseEntity basic implementation
type BaseEntity struct {
	id     string
	kind   string
	x      int
	y      int
	width  int
	height int
}

// GetID returns predefined id
func (b *BaseEntity) GetID() string {
	return id
}

// GetKind type of entity
func (b *BaseEntity) GetKind() string {
	return kind
}

// GetX return most left point
func (b *BaseEntity) GetX() int {
	return x
}

// GetY return topmost point
func (b *BaseEntity) GetY() int {
	return y
}

// GetWidth return width
func (b *BaseEntity) GetWidth() int {
	return width
}

// GetHeight return height
func (b *BaseEntity) GetHeight() int {
	return height
}
