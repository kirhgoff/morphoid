package engine

// Action command to engine to do something with creature
type Action struct {
	CreatureID string
	Type       string
	Params     []interface{}
}