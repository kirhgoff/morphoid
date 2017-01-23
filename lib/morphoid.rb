require "ncurses"

require "./lib/game"
require "./lib/renderer"

module Morphoid
  include Ncurses
  def self.move(direction)
    {:action=>:move, :direction=>direction}
  end

  def self.shoot(direction)
    {:action=>:shoot, :direction=>direction}
  end

  def self.main
    puts "======================"
    puts "starting morphoid v1.0"
    puts "======================"

    Ncurses.initscr
    Ncurses.clear
    begin
      Ncurses.start_color
      # TODO create constants
      Ncurses.init_pair(1, COLOR_GREEN, COLOR_BLACK) # monster
      Ncurses.init_pair(2, COLOR_YELLOW, COLOR_BLACK) # player
      Ncurses.init_pair(3, COLOR_RED, COLOR_BLACK) # bullet


      Ncurses.nonl()
      Ncurses.cbreak()
      Ncurses.noecho()
      Ncurses.curs_set(0)
      Ncurses.stdscr.nodelay(true)
      Ncurses.stdscr.keypad(true)

      #width, height
      lines   = []
      columns = []
      Ncurses.stdscr.getmaxyx(lines,columns)
      height = lines[0]
      width = columns[0]

      game = Game.new(width,height)
      renderer = Renderer.new(game, Ncurses.stdscr)

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
          #puts "Pressed #{chr}, w=#{'w'.ord}"
        end
        sleep(0.05) # TODO create settings file
        renderer.update(Ncurses.stdscr)
        Ncurses.refresh
      end while true
    ensure
      Ncurses.curs_set(1)
      Ncurses.endwin()
    end
  end
end

Morphoid::main()
