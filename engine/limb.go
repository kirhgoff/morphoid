package engine

// Limb : functional part of creature
type Limb interface{}

//-----------------------------------------------

// DummyLimb simple limb for testing purposes
type DummyLimb struct {
	radius      int
	returnValue string
}

// Process just return predefined value
func (limb *DummyLimb) Process(lore Lore) []Signal {
	return []Signal{Action{Type: "type", Params: []interface{}{limb.returnValue}}}
}

// GetVision return predefined value
func (limb *DummyLimb) GetVision() int {
	return limb.radius
}

//-----------------------------------------------

// MateNearbyReceptor checks that mate is nearby
type MateNearbyReceptor struct {
}

// Process sends signal that there are mates around
func (m *MateNearbyReceptor) Process(lore *Lore) []Signal {
	//lore.MapAround(')
	return nil
}
