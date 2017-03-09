package org.kirhgoff.morphoid

import scalafx.Includes._
import scalafx.animation.{KeyFrame, Timeline}
import scalafx.application.{JFXApp, Platform}
import scalafx.beans.property.DoubleProperty
import scalafx.event.ActionEvent
import scalafx.scene.input.{KeyCode, KeyEvent}
import scalafx.scene.paint.Color
import scalafx.scene.text.{Font, Text}
import scalafx.scene.{Cursor, Group, Scene}
import scalafx.stage.Stage

/**
  * Created by <a href="mailto:kirill.lastovirya@gmail.com">kirhgoff</a> on 8/3/17.
  */
object Main extends JFXApp {
  val Human =" @ \n/|\\\n/\\"

  stage = new JFXApp.PrimaryStage {
    fullScreen = true
    title.value = "Morphoid v0.3"

    //val gc = canvas.graphicsContext2D
    val playerPositionX = DoubleProperty(300.0)
    val playerPositionY = DoubleProperty(200.0)
    val random = scala.util.Random

    val player = new Text (Human) {
      x <== playerPositionX
      y <== playerPositionY
      fill = Color.Black
      font = Font("Monospaced", 20)
    }

//    val keyFrame = KeyFrame(10 ms, onFinished = {
//      _ =>
//        playerPositionX() = playerPositionX.value + (0.5 - random.nextDouble())*10
//        playerPositionY() = playerPositionY.value + (0.5 - random.nextDouble())*10
//    })
//
//    val animation = new Timeline {
//      keyFrames = Seq(keyFrame)
//      cycleCount = Timeline.Indefinite
//    }

    val rootPane = new Group {
      children = List(player)
      onKeyPressed = (k: KeyEvent) => k.code match {
          case KeyCode.W =>
            playerPositionY() = playerPositionY.value - 6
          case KeyCode.S =>
            playerPositionY() = playerPositionY.value + 6
          case KeyCode.A =>
            playerPositionX() = playerPositionX.value - 6
          case KeyCode.D =>
            playerPositionX() = playerPositionX.value + 6
          case KeyCode.Escape =>
            stage.close()
          case _ =>
        }
    }

    scene = new Scene {
      content = rootPane
      cursor = Cursor.None
    }

//    animation.playFromStart()

    onCloseRequest = {
      _ =>
        Platform.exit()
        System.exit(0)
    }

    rootPane.requestFocus()

  }
}