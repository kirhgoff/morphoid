require "./lib/player"
require "./lib/bullet"
require "./lib/monster"

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

    def initialize(width,height)
      @width = width
      @height = height

      @player = Player.new(@width/2,@height/2)
      @objects = [@player]
      10.times do
        @objects.push(Monster.new(rand(@width), rand(@height), rand(10), rand()))
      end
    end

    def update(action_map=nil)
      @objects = @objects.select {|object| object.persistent?}
      if !action_map.nil? && @player.alive?
        # Update states for objects
        if action_map[:action] == :move
          dx, dy = SHIFTS[action_map[:direction]]
          @player.move(dx, dy)
        elsif action_map[:action] == :shoot
          @objects.push Bullet.new(@player.x, @player.y, action_map[:direction])
        end
      end

      alive_objects = @objects.select {|object| object.alive?}

      # interactions between objects
      # TODO optimize
      pairs = alive_objects.combination(2).find_all do
        |pair| pair[0].x == pair[1].x && pair[0].y == pair[1].y
      end

      pairs.map do |pair|
        obj1 = pair[0]
        obj2 = pair[1]
        if obj1.instance_of?(Bullet) && obj2.instance_of?(Monster)
          obj1.kill
          obj2.shot(1)
        elsif obj1.instance_of?(Monster) && obj2.instance_of?(Bullet)
          obj1.shot(1)
          obj2.kill
        elsif obj1.instance_of?(Monster) && obj2.instance_of?(Monster)
          if obj1.energy > obj2.energy
            obj1.eat(obj2)
          else
            obj2.eat(obj1)
          end
        elsif obj1.instance_of?(Player) && obj2.instance_of?(Monster)
          obj1.kill
        elsif obj1.instance_of?(Monster) && obj2.instance_of?(Player)
          obj2.kill
        end
      end

      # move objects
      alive_objects.map do |object|
        object.do_step
      end

      # fix their positions
      alive_objects.map do |object|
        fix_object(object)
      end
    end

    def fix_object(object)
      dx, dy = fix(object.x, object.y)
      if dx != 0 || dy != 0
        if object.instance_of?(Bullet)
          object.kill
        else
          object.move(dx, dy)
        end
      end
    end

    def fix(x, y)
      dx = 0
      dy = 0
      dx = @width - 1 - x if x >= @width
      dy = @height - 1 - y if y >= @height
      dx = - x if x < 0
      dy = - y if y < 0
      [dx, dy]
    end
  end
end
