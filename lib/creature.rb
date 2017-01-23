module Morphoid
  class Creature
    attr_accessor :x, :y

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

    # TODO rename to is_alive?
    def alive?
      @alive
    end

    def persistent?
      true
    end

    def kill
      @alive = false
    end
  end
end
