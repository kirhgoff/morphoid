package engine

import (
	. "github.com/onsi/ginkgo"
	. "github.com/onsi/gomega"
)

var _ = Describe("World", func() {
	Context("when created", func() {
		It("it able to provide lore", func() {
			seed := NewSeed("shroom", 0, 0, 0, nil)
			world := NewWorld([]Entity{seed})
			Expect(world.GetEntities()).NotTo(BeEmpty())
		})
	})
})
