package engine

import (
	. "github.com/onsi/ginkgo"
	. "github.com/onsi/gomega"
)

var _ = Describe("World", func() {
	Context("when created", func() {
		It("it able to provide lore", func() {
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
