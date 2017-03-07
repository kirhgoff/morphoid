package org.kirhgoff.munchkin

object Main {
  def main(args: Array[String]) {
    val bot = new MudBot("falloutmud.org", 2222)
//    bot.login("ChosenOne", "~/.chosenone")
//    bot.enterWarzone()
    bot.startLoop()
    bot.stop()
  }
}

