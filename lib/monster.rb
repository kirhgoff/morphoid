module Morphoid
  class Monster < Creature
    attr_reader :energy

    SHOT_DECOY = 3

    def initialize(x,y,energy,speed)
      super(x,y)
      @energy = energy
      @speed = (1/speed).ceil

      # dynamically changed
      @steps = speed
      @shot_decoy = 0
    end

    def alive?
      @energy > 0
    end

    def kill
      @energy = 0
    end

    def do_step
      return unless alive?
      @steps -= 1
      if @steps <= 0
        dx = random_move
        dy = random_move
        move(dx, dy)
        @steps = @speed
      end
    end

    def random_move
      [1, 0, -1].sample
    end

    def render(window)
      color = Ncurses.COLOR_PAIR(1)
      chr = energy.to_s
      if @shot_decoy > 0
        chr = '*'
        color = Ncurses.COLOR_PAIR(3)
        @shot_decoy -= 1
      elsif !alive?
        chr = 'x'
        color = Ncurses.COLOR_PAIR(4) | Ncurses::A_BOLD
      end
      window.attron(color)
      window.mvaddstr(@y, @x, chr)
      window.attroff(color)
    end

    def shot(damage)
      @energy -= damage
      @enery = 0 if @energy < 0
      @shot_decoy = SHOT_DECOY
    end

    def eat(other)
      @energy += other.energy
      other.shot(other.energy)
    end
  end
end
