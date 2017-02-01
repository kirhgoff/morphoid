package genome

import (
	biom "github.com/kirhgoff/morphoid/biom"
)

// Receptor accepts lore and transforms it to internal signal
type Receptor interface {
	Process(lore *biom.Lore) []Signal
}
