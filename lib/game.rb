require "./lib/player"

module Morphoid

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
          if SHIFTS.key?(action)
            dx, dy = SHIFTS[action]
            @player.move(dx, dy)
          end
        elsif action_map[:action] == :shoot
          objects.push Bullet.new(@player.x, @player.y, action_map[:direction])
        end

        # Remove dead objects
        objects.map do |object|
          object.do_step
        end
      end
    end

  end
end
