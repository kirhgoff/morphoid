package org.kirhgoff.munchkin

import java.net.Socket
import java.util.concurrent.Executors._
import java.util.concurrent.ThreadPoolExecutor

class MudBot(hostname: String, port: Int) {
  val executor:ThreadPoolExecutor =
    newFixedThreadPool(2).asInstanceOf[ThreadPoolExecutor]

  val socket = new Socket(hostname, port)
  val outputStream = socket.getOutputStream
  val inputStream = socket.getInputStream

  def stop() = {
    try { inputStream.close() } catch { case e:Exception => e.printStackTrace()}
    try { outputStream.close() } catch { case e:Exception => e.printStackTrace()}
    try { socket.close() } catch { case e:Exception => e.printStackTrace()}
  }

  def startLoop() = {
    val writer = new MudWriter(outputStream)
    val ai = new FalloutBot (writer)
    val inputReader = new UserInputReader(ai)
    val reader = new MudReader(inputStream, ai)

    executor.execute(reader)
    executor.execute(inputReader)

    while (executor.getActiveCount == 2) {
      Thread.sleep(1000)
    }
    println("system> Exiting")
    executor.shutdownNow()
    System.exit(0)
  }

  //def enterWarzone(): Unit = ???

  //def login(login: String, passwordPath: String): Unit = ???

}
