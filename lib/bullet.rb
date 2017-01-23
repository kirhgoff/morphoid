require './lib/creature'
require './lib/game'

module Morphoid
  class Bullet < Creature
    attr_reader :direction
    def initialize(x,y,direction, decoy_length=20)
      super(x,y)

      @initial_x = x
      @initial_y = y

      @direction = direction
      @decoy_length = [:left, :right].include?(direction) ? decoy_length * 2 : decoy_length
    end

    def render(window)
      window.attron(Ncurses.COLOR_PAIR(3) | Ncurses::A_BOLD)
      window.mvaddstr(@y,@x, symbol)
      window.attron(Ncurses.COLOR_PAIR(3) | Ncurses::A_BOLD)
    end

    def symbol
      if @direction == :up || @direction == :down
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

    def persistent?
     alive?
    end
  end
end
