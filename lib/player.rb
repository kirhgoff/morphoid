require './lib/creature'

module Morphoid
  class Player < Creature

    def initialize(x, y)
      super(x,y)
    end

    def render(window)
      window.attron(Ncurses.COLOR_PAIR(2) | Ncurses::A_BOLD)
      window.mvaddstr(@y,@x, alive? ? "@" : "X")
      window.attroff(Ncurses.COLOR_PAIR(2) | Ncurses::A_BOLD)
    end

  end
end
