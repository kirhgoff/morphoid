require './lib/creature'

module Morphoid
  class Player < Creature

    def initialize(x, y)
      super(x,y)
    end

    def render(window)
      window.mvaddstr(@y,@x, "@")
    end

  end
end
