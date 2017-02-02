package genome

import (
	biom "github.com/kirhgoff/morphoid/biom"
	. "github.com/onsi/ginkgo"
	. "github.com/onsi/gomega"
)

// ALlInOneLimb test limb
type AllInOneLimb struct{}

func (limb *AllInOneLimb) Process(lore *biom.Lore) []Signal {
	return []Signal{Action{Type: "type", Params: []interface{}{"data"}}}
}

//func (limb *AllInOneLimb) Limb() {}

var _ = Describe("Creature", func() {
	Context("After creation", func() {
		It("should provide receptors and actors", func() {
			creature := NewCreature("sample", []Limb{new(AllInOneLimb)})
			Expect(len(creature.Limbs)).To(Equal(1))
			Expect(len(creature.Receptors)).To(Equal(1))
			actions := creature.Process(nil)
			Expect(len(actions)).To(Equal(1))

			action := actions[0]
			Expect(action.CreatureID).To(Equal(creature.CreatureID))
			Expect(action.Params[0]).To(Equal("data"))
		})
	})
})
