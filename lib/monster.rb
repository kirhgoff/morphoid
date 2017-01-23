module Morphoid
  class Monster < Creature
    attr_reader :energy

    def initialize(x,y,energy)
      super(x,y)
      @energy = energy
    end

    def alive?
      @energy > 0
    end

    def kill
      @energy = 0
    end

    def do_step
      return unless alive?
      dx = random_move
      dy = random_move
      move(dx, dy)
    end

    def random_move
      [1, 0, -1].sample
    end

    def render(window)
      window.mvaddstr(@y,@x, energy.to_s)
    end

    def shot(damage)
      @energy -= damage
      @enery = 0 if @energy < 0
    end

    def eat(other)
      @energy += other.energy
      other.shot(other.energy)
    end
  end
end
