package engine

// Receptor accepts lore and transforms it to internal signal
type Receptor interface {
	Limb
	Process(lore Lore) []Signal
	GetVision() int
}
