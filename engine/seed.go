package engine

// Seed creates alive being in appropriate conditions:w
type Seed struct {
	kind   string
	energy int // Shows how expressive genome is
	genome []Gene
}

// NewSeed creates new seed
func NewSeed(kind string, energy int, genome []Gene) Seed {
	return Seed{kind: kind, energy: energy, genome: genome}
}

// Produce gives birth to new creature
// goes through all genes until no gene
// can express
// TODO greedy expression
func (seed *Seed) Produce() Creature {
	limbs := make([]Limb, 0, 5)
	for {
		expressing := false
		for _, gene := range seed.genome {
			cost := gene.Cost()
			if cost <= seed.energy {
				limbs = append(limbs, gene.Produce())
				seed.energy -= cost
				expressing = true
			}
		}
		if !expressing {
			break
		}
	}
	//TODO link limbs
	return NewCreature(seed.kind, limbs)
}
