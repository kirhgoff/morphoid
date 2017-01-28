package genome

// Gene : describes limbs that could be produced by Seed
type Gene interface {
	// Cost returns amount of seed energy
	// required to produce this kind of limb
	Cost() int
	// Produce creates new limb relates to that Gene
	Produce() Limb
}
