package engine

import (
	. "github.com/onsi/ginkgo"
	. "github.com/onsi/gomega"
)

var _ = Describe("World", func() {
	var (
		world    World
		creature Creature
	)

	BeforeSuite(func() {
		seed := NewSeed("shroom", 0, 0, 0, nil)
		creature = NewCreature("mouth", 2, 2, []Limb{})
		world = NewWorld([]Entity{seed, creature})
	})

	Context("when created", func() {
		It("can provide entities and creatures", func() {
			Expect(len(world.GetEntities())).To(Equal(2))
			Expect(len(world.GetCreatures())).To(Equal(1))
		})

		It("can feed lore to creatures", func() {
			lore := world.GetLore(creature)
			Expect(lore.Radius()).To(Equal(creature.GetVision()))
			//Case for shrooms
			Expect(lore.Reduce("pepe")).To(Equal(0))
			Expect(lore.Reduce("shroom")).To(Equal(1))
		})
	})
})
