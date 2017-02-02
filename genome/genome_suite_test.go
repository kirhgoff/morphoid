package genome_test

import (
	. "github.com/onsi/ginkgo"
	. "github.com/onsi/gomega"

	"testing"
)

func TestGenome(t *testing.T) {
	RegisterFailHandler(Fail)
	RunSpecs(t, "Genome Suite")
}
