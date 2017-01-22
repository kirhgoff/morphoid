module Morphoid
  class Creature
    attr_reader :alive, :x, :y

    def initialize(x, y)
      @alive = true
      @x = x
      @y = y
    end

    def move(dx, dy)
      @x += dx
      @y += dy
    end

    def do_step
    end

    def alive?
      true
    end
  end
end
