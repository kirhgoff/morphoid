require "./lib/player"
require "./lib/bullet"

module Morphoid
  # TODO move out to separate file
  SHIFTS = {
    :up=>[0,-1],
    :down=>[0,1],
    :left=>[-1,0],
    :right=>[1,0]
  }

  class Game
    attr_reader :objects

    def initialize
      @player = Player.new(15,15)
      @objects = [@player]
    end

    def update(action_map=nil)
      @objects = @objects.select {|object| object.alive?}
      if !action_map.nil?
        # Update states for objects
        if action_map[:action] == :move
          dx, dy = SHIFTS[action_map[:direction]]
          @player.move(dx, dy)
        elsif action_map[:action] == :shoot
          objects.push Bullet.new(@player.x, @player.y, action_map[:direction])
        end
      end

      objects.map do |object|
        object.do_step
      end
    end

  end
end
