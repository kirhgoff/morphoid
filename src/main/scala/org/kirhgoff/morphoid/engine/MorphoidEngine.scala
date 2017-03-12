package org.kirhgoff.morphoid.engine

import scala.collection.JavaConverters._

/**
  * Created by <a href="mailto:kirill.lastovirya@gmail.com">kirhgoff</a> on 12/3/17.
  */
class MorphoidEngine {
  def getEntities: List[Entity] = List()
  def getEntitiesJava = getEntities.asJava
}

object MorphoidEngine {
  def createSample = new MorphoidEngine
}
