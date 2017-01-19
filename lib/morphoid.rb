require "ncurses"

require "./lib/game"
require "./lib/renderer"

module Morphoid
  puts "======================"
  puts "starting morphoid v1.0"
  puts "======================"

  Ncurses.initscr
  Ncurses.clear
  begin
    if (Ncurses.has_colors?)
      bg = Ncurses::COLOR_BLACK
      Ncurses.start_color
      if (Ncurses.respond_to?("use_default_colors"))
        if (Ncurses.use_default_colors == Ncurses::OK)
          bg = -1
        end
      end
    end
    Ncurses.nonl()
    Ncurses.cbreak()
    Ncurses.noecho()
    Ncurses.curs_set(0)
    Ncurses.stdscr.nodelay(true)
    Ncurses.stdscr.keypad(true)

    game = Game.new()
    renderer = Renderer.new(game, Ncurses.stdscr)

    begin
      case(chr = Ncurses.getch())
      when 'q'.ord, 'Q'.ord
        Ncurses.curs_set(1)
        Ncurses.endwin()
        exit
      when Ncurses::KEY_UP
        game.update(:up)
      when Ncurses::KEY_DOWN
        game.update(:down)
      when Ncurses::KEY_LEFT
        game.update(:left)
      when Ncurses::KEY_RIGHT
        game.update(:right)
      when Ncurses::KEY_RESIZE
        renderer.sigwinch_handler
      else
        #puts "Pressed #{chr}, key_up=#{Ncurses::KEY_UP}"
      end
      sleep(0.050)
      renderer.update(Ncurses.stdscr)
      Ncurses.refresh
    end while true
  ensure
    Ncurses.curs_set(1)
    Ncurses.endwin()
  end

end
