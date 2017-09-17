package org.kirhgoff.morphoid.engine

import org.kirhgoff.morphoid.PlayerInputState

import scala.collection.{JavaConverters, mutable}

abstract class GameEvent(sourceId:String, targetId:String)
case class CreatureMoves(sourceId:String, targetId:String, direction:Direction) extends GameEvent(sourceId, targetId)
case class CreatureAttacks(sourceId:String, targetId:String, direction:Direction) extends GameEvent(sourceId, targetId)
case class CreatureObserve(sourceId:String, targetId:String, surroundings:List[Cell]) extends GameEvent(sourceId, targetId)

// Information Psyche can ask about
trait Lore {
  def kindsInside(cell:Cell):List[String]
}

/**
  * Not thread safe
  *
  * Created by <a href="mailto:kirill.lastovirya@gmail.com">kirhgoff</a> on 12/3/17.
  */
class MorphoidEngine (val levelRect:Rect, initialEntities:List[Psyche])
  extends Lore {
  val GOD_ENGINE = "GOD ENGINE v.01"

  private val player = initialEntities.head
  private var creatures =  mutable.Map[String, Creature](initialEntities map (p => p.id -> p.creature): _*)
  private val souls = mutable.Map[String, Psyche](initialEntities map (p => p.id -> p): _*)

  //TODO implement surroundings properly
  def surroundings(creature: Creature):List[Cell] = List()

  // UI Interface
  def getEntities: List[Creature] = creatures.values.toList
  def getEntitiesJava = JavaConverters.asJavaCollection(getEntities)

  private def addEntity(psyche: Psyche) = {
    val creature = psyche.creature
    creatures(creature.id) = creature
    souls(psyche.id) = psyche
    psyche.setEngine(this)
  }

  def init() = {
    initialEntities.foreach(_.setEngine(this))
    this
  }

  def tick() {
    val actions = souls.values.map(p => {
      // Makes sense to keep frequency information in the engine
      if (p.tick) p.act(surroundings(p.creature))
      else List() // make it better
    })

    actions.foreach(batch => execute(validate(batch)))
  }

  def validate(events: List[GameEvent]):List[GameEvent] = {
    events.filter(event => event match {
      case CreatureMoves(_, id, direction) => {
        val rect = creatures(id).boundingRect.move(direction)
        val intersectsWithSomething = creatures.values.map(_.boundingRect).exists(_.intersects(rect))
        val insideBorders = levelRect.includes(rect)
        !intersectsWithSomething && insideBorders
      }
      case _ => true
    })
  }

  def execute(events: List[GameEvent]) = events match {
    case Nil =>
    case List() =>
    case event :: rest => event match {
      case CreatureMoves(_, id, direction) => {
        creatures(id).move(direction)
      }
      case CreatureAttacks(_, id, direction) => {
        val creature = creatures(id)
        creature.attack(direction)
        val newId = Dice.makeId("pew")
        val projectileOrigin = creature.origin.nextTo(direction)
        val projectile = new Creature(newId, "projectile", List(projectileOrigin))
        addEntity(new Projectile(newId, direction, 5, projectile))
      }
    }

  }

  override def kindsInside(cell: Cell) = {
    //Naive implementation
    List() //TODO implement Table class
  }
}

object MorphoidEngine {
  def apply(psyche: Psyche*) = new MorphoidEngine(Rect(0, 0, 10, 10), psyche.toList).init
  def createSample(width:Int, height:Int, playerInputState: PlayerInputState) = new MorphoidEngine (
    Rect(0, 0, width, height),
    List(
      PlayerSoul(playerInputState, 10, 10, 5),
      Herbivore(3, 5, 40),
      Herbivore(15, 15, 60),
      Herbivore(9, 11, 50),
      Plant(1, 8),
      Plant(6, 13),
      Plant(16, 12)
    )
  )
}



