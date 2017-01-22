module Morphoid
  class Bullet < Creature

    def initialize(x,y,direction)
      super(x,y)

      @initial_x = x
      @initial_y = y

      @direction = direction
      @decoy_length = 5
    end

    def render(window)
      window.mvaddstr(@y,@x, symbol)
    end

    def symbol
      if !alive?
        "*"
      elsif @direction == :up || @direction == :down
        "|"
      elsif @direction == :left || @direction == :right
        "-"
      else
        "?"
      end
    end

    def do_step
      shifts = SHIFTS[@direction]
      @x += shifts[0]
      @y += shifts[1]
      @alive = (@initial_x + @initial_y - @x - @y).abs < @decoy_length
    end
  end
end
