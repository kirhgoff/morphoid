package engine

import (
	. "github.com/onsi/ginkgo"
	. "github.com/onsi/gomega"
)

var _ = Describe("Creature", func() {
	Context("After creation", func() {
		It("should provide receptors and actors", func() {
			creature := NewCreature("sample", 0, 1, []Limb{DummyLimb{666, "data"}})
			Expect(creature.GetX()).To(Equal(0))
			Expect(creature.GetY()).To(Equal(1))
			Expect(len(creature.GetLimbs())).To(Equal(1))
			Expect(len(creature.GetReceptors())).To(Equal(1))
			Expect(creature.GetVision()).To(Equal(666))
			actions := creature.Process(nil)
			Expect(len(actions)).To(Equal(1))

			action := actions[0]
			Expect(action.CreatureID).To(Equal(creature.GetID()))
			Expect(action.Params[0]).To(Equal("data"))
		})
	})
})
