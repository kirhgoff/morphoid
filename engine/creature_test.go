package engine

import (
	. "github.com/onsi/ginkgo"
	. "github.com/onsi/gomega"
)

// ALlInOneLimb test limb
type AllInOneLimb struct{}

func (limb *AllInOneLimb) Process(lore Lore) []Signal {
	return []Signal{Action{Type: "type", Params: []interface{}{"data"}}}
}

var _ = Describe("Creature", func() {
	Context("After creation", func() {
		It("should provide receptors and actors", func() {
			creature := NewCreature("sample", 0, 0, []Limb{new(AllInOneLimb)})
			Expect(len(creature.GetLimbs())).To(Equal(1))
			Expect(len(creature.GetReceptors())).To(Equal(1))
			actions := creature.Process(nil)
			Expect(len(actions)).To(Equal(1))

			action := actions[0]
			Expect(action.CreatureID).To(Equal(creature.GetID()))
			Expect(action.Params[0]).To(Equal("data"))
		})
	})
})
