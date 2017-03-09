package org.kirhgoff.morphoid

import scalafx.Includes._
import scalafx.animation.{KeyFrame, Timeline}
import scalafx.application.JFXApp
import scalafx.beans.property.DoubleProperty
import scalafx.event.ActionEvent
import scalafx.scene.paint.Color
import scalafx.scene.text.Text
import scalafx.scene.{Group, Scene}

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
    }

    val keyFrame = KeyFrame(10 ms, onFinished = {
      event: ActionEvent =>
        playerPositionX() = playerPositionX.value + (0.5 - random.nextDouble())*10
        playerPositionY() = playerPositionY.value + (0.5 - random.nextDouble())*10
    })

    val animation = new Timeline {
      keyFrames = Seq(keyFrame)
      cycleCount = Timeline.Indefinite
    }

    val rootPane = new Group(player)

    scene = new Scene {
      content = rootPane
    }

    animation.playFromStart()
  }
}