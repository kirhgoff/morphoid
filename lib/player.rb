module Morphoid
  class Player
    def initialize(x, y)
      @x = x
      @y = y
    end

    def move(dx, dy)
      @x += dx
      @y += dy
    end

    def random_shift
      [true, false].sample ? 1 : -1
    end

    def render(window)
      window.mvaddstr(@y,@x, "@")
    end
  end
end
