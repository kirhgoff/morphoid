require "ncurses"

module Morphoid
  class Renderer
    def initialize(game,window)
      @game = game
      @window = window
      @window_size_changed = true
      @has_colors = Ncurses.has_colors?
    end

    # handler for signal that window size changed
    def sigwinch_handler(sig = nil)
      @window_size_changed = true
    end

    def update(window)
      #adjust_to_new_window_size if (@window_size_changed)
      window.clear
      @game.update()
      @game.objects.map do |object|
        object.render(window) # TODO extract render logic
      end
    end
  end
end
