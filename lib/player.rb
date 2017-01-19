module Morphoid
  class Player
    def initialize(x, y)
      @x = x
      @y = y
    end

    def roam
      @x += random_shift
      @y += random_shift
    end

    def random_shift
      [true, false].sample ? 1 : -1
    end

    def render(window)
      window.mvaddstr(@y,@x, "K")
    end
  end
end
