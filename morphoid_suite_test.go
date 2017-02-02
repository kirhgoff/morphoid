package main_test

import (
	"testing"

	. "github.com/onsi/ginkgo"
	. "github.com/onsi/gomega"
)

func TestMorphoid(t *testing.T) {
	RegisterFailHandler(Fail)
	RunSpecs(t, "Morphoid Suite")
}
