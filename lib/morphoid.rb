require "ncurses"

require "./lib/game"
require "./lib/renderer"

module Morphoid
  puts "======================"
  puts "starting morphoid v1.0"
  puts "======================"

  Ncurses.initscr
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
    Ncurses.noecho()
    Ncurses.curs_set(0)
    Ncurses.stdscr.nodelay(true)

    game = Game.new()
    renderer = Renderer.new(game, Ncurses.stdscr)

    begin
      case(chr = Ncurses.getch())
      when 'q'[0], 'Q'[0]
        Ncurses.curs_set(1)
        Ncurses.endwin()
        exit
      when Ncurses::KEY_RESIZE
        renderer.sigwinch_handler
      else
        puts "Received #{chr}"
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
