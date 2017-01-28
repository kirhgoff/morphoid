package genome_test

import (
	. "github.com/kirhgoff/morphoid/genome"

	. "github.com/onsi/ginkgo"
	. "github.com/onsi/gomega"
)

var _ = Describe("Seed", func() {
	Context("With simple seed", func() {
		It("should create creature", func() {
			seed := NewSeed("sample", 0, make([]Gene, 0))
			creature := seed.Produce()
			Expect(creature.Kind).To(Equal("sample"))
		})
	})
})
