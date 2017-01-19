require "./lib/player"

module Morphoid
  class Game
    SHIFTS = {:up=>[0, -1], :down=>[0,1], :left=>[-1,0], :right=>[1,0]}
    def initialize
      @player = Player.new(15,15)
    end

    def update(action = nil)
      if SHIFTS.key?(action)
        dx, dy = SHIFTS[action]
        @player.move(dx, dy)
      end
    end

    def get_objects
      [@player]
    end
  end
end
