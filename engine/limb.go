package engine

// Limb : functional part of creature
type Limb interface{}

//-----------------------------------------------

// MateNearbyReceptor checks that mate is nearby
type MateNearbyReceptor struct {
}

// Process sends signal that there are mates around
func (m *MateNearbyReceptor) Process(lore *Lore) []Signal {
	//lore.MapAround(')
	return nil
}
