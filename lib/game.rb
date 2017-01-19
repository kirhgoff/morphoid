require "./lib/player"

module Morphoid
  class Game
    def initialize
      @player = Player.new(15,15)
    end

    def update
      @player.roam
    end

    def get_objects
      [@player]
    end
  end
end
