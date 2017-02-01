package genome_test

import (
	. "github.com/kirhgoff/morphoid/genome"

	. "github.com/onsi/ginkgo"
	. "github.com/onsi/gomega"
)

// ALlInOneLimb test limb
type AllInOneLimb struct {}

func (limb * AllInOneLimb) Process(lore * Lore) Signal {
	return StringSignal{}
}

var _ = Describe("Creature", func() {
	Context("After creation", func() {
		It("should provide receptors and actors", func() {
			receptor = 
			seed := NewCreature("sample", )
			creature := seed.Produce()
			Expect(creature.Kind).To(Equal("sample"))
		})
	})
})

