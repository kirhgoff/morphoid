package org.kirhgoff.munchkin

import java.nio.charset.StandardCharsets._
import java.nio.file.{Files, Paths}

/**
+----+----+----+----+----+----+
|                             |      01 Entrance
|                             |      02 Exit (up)
+    +----+    +    +----+    +      03 Sanford Son's shop
|    |    |         |    |    |
|    |              |    |    |
+    +----+    +    +-  -+    +----+
| 02                            01 |
|                                  |
+    +-  -+    +    +-  -+    +----+
|    | 03 |         |    |    |
|    |    |         |    |    |
+    +----+    +    +----+    +
|                             |
|                             |
+----+----+----+----+----+----+
  */

import org.kirhgoff.munchkin.api.{CommandProcessor, GameStateParser}

class FalloutBot(val commandsProcessor:CommandProcessor)
  extends GameStateParser with CommandProcessor {

  val LoreFileName = "/tmp/fallout_lore.txt"
  val writer = Files.newBufferedWriter(Paths.get(LoreFileName), UTF_8)

  override def parseIncomingLore(data:String) = {
    val strippedData = data.replaceAll("\u001B\\[[;\\d]*m", "")
    writer.write(strippedData)
    writer.flush()
  }

  override def issueCommand(command: String) = {
    if (command.startsWith("bot:")) {
      println(s"Bot> command $command")
    } else {
      commandsProcessor.issueCommand(command)
    }
  }
}
