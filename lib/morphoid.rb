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

  def move(direction)
    {:action=>:move, :direction=>direction}
  end

  def shoot(direction)
    {:action=>:shoot, :direction=>direction}
  end

    begin
      case(chr = Ncurses.getch())
      when 'q'.ord, 'Q'.ord
        Ncurses.curs_set(1)
        Ncurses.endwin()
        exit
      when 'a'.ord, 'A'.ord
        game.update(move(:left))
      when 'w'.ord, 'W'.ord
        game.update(move(:up))
      when 's'.ord, 'S'.ord
        game.update(move(:down))
      when 'd'.ord, 'D'.ord
        game.update(move(:right))
      when Ncurses::KEY_LEFT
        game.update(:left)
      when Ncurses::KEY_RIGHT
        game.update(:right)
      when Ncurses::KEY_UP
        game.update(shoot(:up))
      when Ncurses::KEY_DOWN
        game.update(shoot(:down))
      when Ncurses::KEY_LEFT
        game.update(shoot(:left))
      when Ncurses::KEY_RIGHT
        game.update(shoot(:right))
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
