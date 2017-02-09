package engine

import uuid "github.com/nu7hatch/gouuid"

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

// NewEntity create new entity
func NewEntity(kind string, x, y, width, height int) *BaseEntity {
	uuid, err := uuid.NewV4()
	if err != nil {
		panic(err)
	}

	return &BaseEntity{
		id:     uuid.String(),
		kind:   kind,
		x:      x,
		y:      y,
		width:  width,
		height: height,
	}
}

// GetID returns predefined id
func (b *BaseEntity) GetID() string {
	return b.id
}

// GetKind type of entity
func (b *BaseEntity) GetKind() string {
	return b.kind
}

// GetX return most left point
func (b *BaseEntity) GetX() int {
	return b.x
}

// GetY return topmost point
func (b *BaseEntity) GetY() int {
	return b.y
}

// GetWidth return width
func (b *BaseEntity) GetWidth() int {
	return b.width
}

// GetHeight return height
func (b *BaseEntity) GetHeight() int {
	return b.height
}
