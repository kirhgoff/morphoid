package org.kirhgoff.morphoid.engine

trait EngineHelperTrait {

  def findCreatureByType(creature: String)(implicit engine: MorphoidEngine) =
    engine.getCreatures.find(c => c.kind.equals(creature)).get

  def creatures(implicit engine: MorphoidEngine): List[Creature] =
    engine.getCreatures.filter(_.kind != "decoy")

  def decoy(implicit engine: MorphoidEngine): List[Creature] =
    engine.getCreatures.filter(_.kind == "decoy")

  def fullEnergy(implicit engine: MorphoidEngine): Double =
    engine.getCreatures.map(_.energy).sum

  def allCells(implicit engine: MorphoidEngine) =
    engine.getCreatures.flatMap(_.cellsMap.values).map(_.toString)
}
