package engine

// Receptor accepts lore and transforms it to internal signal
type Receptor interface {
	Process(lore Lore) []Signal
}
