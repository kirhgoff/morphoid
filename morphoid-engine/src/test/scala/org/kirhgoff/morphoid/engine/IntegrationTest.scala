package org.kirhgoff.morphoid.engine

import org.kirhgoff.morphoid.{PlayerInputState}
import org.scalamock.scalatest.MockFactory
import org.scalatest.{FlatSpec, Matchers}

/**
  * Created by <a href="mailto:kirill.lastovirya@gmail.com">kirhgoff</a> on 11/10/17.
  */
class IntegrationTest extends FlatSpec with Matchers with MockFactory {
  "ProductionConfiguration" should "be able to work 100 ticks" in {
    val scenariosToCheck = List("production", "simple")
    scenariosToCheck.foreach(scenario => {
      val playerInputState = new PlayerInputState
      val engine = MorphoidEngine.create(scenario, playerInputState).init()
      //val playerController = new PlayerController(playerInputState)
      try {
        for (_ <- 0 to 100) {
          engine.tick()
          engine.getCreaturesJava should not be null
        }
      } catch {
        case e: Exception => fail(e)
      }
    })
  }
}
