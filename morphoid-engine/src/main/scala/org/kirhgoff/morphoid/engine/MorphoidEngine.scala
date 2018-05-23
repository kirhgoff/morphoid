package org.kirhgoff.morphoid.engine

import org.kirhgoff.morphoid.PlayerInputState
import org.kirhgoff.morphoid.engine.Dice._

import scala.collection.{JavaConverters, mutable}

abstract class GameEvent(soulId:String, creatureId:String)
case class CreatureMoves(soulId:String, creatureId:String, direction:Direction) extends GameEvent(soulId, creatureId)
case class CreatureAttacks(soulId:String, creatureId:String, direction:Direction) extends GameEvent(soulId, creatureId)
case class CreatureObserve(soulId:String, creatureId:String, surroundings:List[Physical]) extends GameEvent(soulId, creatureId)

class Cell(var kind:String, var cellType:CellType)

// Information Psyche can ask about
trait Lore {
  // Key is Physical.toString
  private val creatureIndex = mutable.Map[String, Creature]()
  // Key is Physical.toString
  private val lore = mutable.Map[String, Cell]()

  private def find(physical: Physical):Cell =
    lore.getOrElseUpdate(physical.toString, new Cell("", Dummy))

  def creatureType(physical: Physical) = find(physical).kind
  def cellType(physical: Physical) = find(physical).cellType

  def registerCreature(creature: Creature) = {
    creature.cells.foreach(physical => {
      val cell = find(physical)
      cell.kind = creature.kind
      cell.cellType = creature.cellType(physical)
      creatureIndex(physical.toString) = creature
    })
  }

  def unregisterCreature(creature:Creature) = {
    creature.cells.foreach(physical => {
      val cell = find(physical)
      cell.kind = ""
      cell.cellType = Dummy
      creatureIndex(physical.toString) = null
    })
  }
}

/**
  * Not thread safe
  * Mutable
  *
  * Created by <a href="mailto:kirill.lastovirya@gmail.com">kirhgoff</a> on 12/3/17.
  */
class MorphoidEngine (val levelRect:Rect, initialEntities:List[Psyche])
  extends Lore {
  val GOD_ENGINE = "GOD ENGINE v.03" //-> multi-cell organisms

  //private val player = initialEntities.head
  private val souls = mutable.Map[String, Psyche](initialEntities map (p => p.id -> p): _*)
  private val creatures =  mutable.Map[String, Creature](initialEntities map (p => p.creature.id -> p.creature): _*)

  private var energyBalanceController = EnergyBalanceController.simple()

  def setEnergyBalanceController(controller:EnergyBalanceController): MorphoidEngine = {
    energyBalanceController = controller
    this
  }

  def whoIsHere(cell:Physical) = s"${creatureType(cell)}$cell"

  //TODO implement surroundings properly
  def surroundings(creature: Creature, sight:Int):List[Physical] = {
    creature.cells.flatMap(c => Rect.inflate(c, sight).decompose)
  }

  //Test interface
  def soulById(id:String) = souls(id)

  // UI Interface
  def getCreatures: List[Creature] = creatures.values.toList
  def getCreaturesJava = JavaConverters.asJavaCollection(getCreatures)

  def init() = {
    initialEntities.foreach(psyche => {
      psyche.setEngine(this)

      val creature = psyche.creature
      creature.updateEnergy(initialEnergy(creature))
      registerCreature(creature)
    })
    this
  }

  // TODO move to controller
  def initialEnergy(creature: Creature):Double = creature.kind match {
    case "shroom" => energyBalanceController.shroomLife
    case "ooze" => energyBalanceController.oozeLife
    case "player" => energyBalanceController.playerLife
    case "projectile" => energyBalanceController.projectile
    case "decoy" => 0.0 // it is already set
    case _ => -666 // To easily notice if forgot to add
  }

  def createDecoy(creature: Creature):Psyche = Decoy.fromDead(creature)

  def tick() = {
//    println(s"MEngine.tick() ${Dice.nextTickNumber} ------------------\n " +
//      s"souls: ${souls.keys} creatures: ${creatures.keys} cells: ${creatures.values.flatMap(_.cellsMap).map(_.toString)}")

    val entitiesMap = souls.values.groupBy(c => if (c.creature.kind == "decoy") {
      "decoy"
    } else if (c.creature.energy <= energyBalanceController.decoyThreshold) {
      "dead"
    } else {
      "alive"
    })

    val decoy = entitiesMap.getOrElse("decoy", List())
    val dead = entitiesMap.getOrElse("dead", List())
    val alive = entitiesMap.getOrElse("alive", List())

    decoy.foreach(psyche => {
//      println(s"Checking decoy: ${psyche.creature}")

      if (psyche.creature.energy <= energyBalanceController.completeDecoy) {
//        println(s"Removing!")
        removePsyche(psyche)
      }
    })

    // Turn dead to decay
    dead.foreach(psyche => {
      removePsyche(psyche)
      addPsyche(createDecoy(psyche.creature))
    })

    alive
      .filter(_.readyToAct)
      .foreach(p => {
        val sur = surroundings(p.creature, p.sight)
        val batch = p.act(sur)
        //      println(s"creature ${p.creature}" +
        //        s"\n\tsurr=${sur.map(s => str(s)).mkString(" ")}" +
        //        s"\n\tbatch=$batch")
        execute(validate(batch))
    })

    creatures.values.foreach(creature => {
      val energyUpdate = creature.cells
        .foldLeft(0.0)((a, c) => a + energyGrowth(cellType(c)))

//      println(s"Updating creature: ${creature}" +
//        s" adding energy: $energyUpdate")

      creature.updateEnergy(energyUpdate)
    })

//    println(s"MEngine.tick() end: ${entitiesMap.size}")
    this
  }

  private def removePsyche(psyche: Psyche) = {
//    println(s">>> Souls size: ${souls.size} creatures: ${creatures.keys}")

    souls.remove(psyche.id)
    creatures.remove(psyche.creature.id)
    unregisterCreature(psyche.creature)
//    println(s"Removing creature: ${psyche.creature.id}")
//    println(s"<<< Souls size: ${souls.size} creatures: ${creatures.keys}")
  }

  private def addPsyche(psyche: Psyche) = {
    souls.put(psyche.id, psyche)
    creatures.put(psyche.creature.id, psyche.creature)
    psyche.setEngine(this)
    registerCreature(psyche.creature)
  }

  // TODO move to controller
  def energyGrowth(cellType:CellType):Double = cellType match {
    case _:Seed => energyBalanceController.seedGrowth
    case _:ShroomSeed => energyBalanceController.shroomIncrease
    case _ => energyBalanceController.cellDecay
  }


  def validate(events: List[GameEvent]):List[GameEvent] = {
    events.filter(event => event match {
      case CreatureMoves(_, id, direction) => {
        val creature = creatures(id)
        val rect = creature.boundingRect.move(direction)
        val intersectsWithSomething = others(creature).map(_.boundingRect).exists(_.intersects(rect))
        val insideBorders = levelRect.includes(rect)
        //println(s"Validating creature $creatures(id) direction=$direction iws=$intersectsWithSomething ib=$insideBorders")
        !intersectsWithSomething && insideBorders
      }
      case _ => true
    })
  }

  private def others(creature: Creature) = {
    creatures.values.filter(creature != _)
  }

  def isPlayer(creature: Creature) = "player".equals(creature.kind)

  def execute(events: List[GameEvent]): Unit = events match {
    case Nil =>
    case List() =>
    case event :: rest => {
      //println(s"Processing $event")
      event match {
        case CreatureMoves(_, id, direction) => {
          val creature = creatures(id)
          //println(s"execute creatureMoves=$creature direction=$direction")

          // Update cell indices
          unregisterCreature(creature)
          registerCreature(creature.move(direction))
        }
        case CreatureAttacks(_, creatureId, direction) => {
          val attacker = creatures(creatureId)
          val rect = attacker.boundingRect

          others(attacker).find(p => rect.touches(p.boundingRect, direction)) match {
            case Some(prey) => {
              //println(s"Attacker: $creatureId, prey: $prey rect=$rect")
              attacker.updateEnergy(EnergyBalanceController.simple().oozeAttack)
              prey.updateEnergy(-EnergyBalanceController.simple().oozeAttack)
            }
            case None => {
              if (isPlayer(attacker)) {
                val newId = Dice.makeId("pew")
                val projectileOrigin = attacker.origin.nextTo(direction)
                val projectile = new Creature(newId, "projectile", Map(projectileOrigin -> new Seed))
                addPsyche(new Projectile(newId, direction, 5, projectile))
              }
            }
          }
        }
      }

      execute(rest)
    }
  }
}

object MorphoidEngine {
  def apply(energyBalanceController: EnergyBalanceController, psyche: Psyche*) = {
    val engine = new MorphoidEngine(Rect(0, 0, 10, 10), psyche.toList)
    engine.setEnergyBalanceController(energyBalanceController)
    engine.init()
  }

  def apply(psyche: Psyche*) = new MorphoidEngine(Rect(0, 0, 10, 10), psyche.toList).init()

  def create(scenario:String, playerInputState: PlayerInputState) = scenario match {
    case "simple" => createSimple(20, 20, playerInputState)
    case "empty" => createEmpty(20, 20, playerInputState)
    case "lonely" => createLonely(20, 20, playerInputState)
    case _ => createProduction(playerInputState)
  }

  // TODO extract method
  def createEmpty(width:Int, height:Int):MorphoidEngine =
    createEmpty(width, height, new PlayerInputState)

  def createEmpty(width:Int, height:Int, playerInputState: PlayerInputState)
  = new MorphoidEngine(
    Rect(0,0, width, height),
    List(
      PlayerSoul(playerInputState, 10, 10, 5)
    )
  ).init()

  def createLonely(width:Int, height:Int, playerInputState: PlayerInputState) = new MorphoidEngine (
    Rect(0, 0, width, height),
    List(
      PlayerSoul(playerInputState, 10, 10, 5),
      Ooze(3, 5, 40),
      Decoy(4, 6, 10.0)
    )
  ).setEnergyBalanceController(EnergyBalanceController.realistic()).init()


  def createSimple(width:Int, height:Int, playerInputState: PlayerInputState) = new MorphoidEngine (
    Rect(0, 0, width, height),
    List(
      PlayerSoul(playerInputState, 10, 10, 5),
      Ooze(3, 5, 40),
      Ooze(15, 15, 60),
      Ooze(19, 19, 60),
      Ooze(9, 11, 50),
      Shroom(1, 8),
      Shroom(6, 13),
      Shroom(16, 12),
      Decoy(4, 6, 10.0)
    )
  ).setEnergyBalanceController(EnergyBalanceController.realistic()).init()

  def createProduction(playerInputState: PlayerInputState) = {
    val width = 30
    val height = 30

    val plantsCount = 20
    val cowsCount = 50

    val minSpeed = 20
    val speedDiff = 10

    val plants:List[Psyche] = (1 to plantsCount)
      .map(_ => Shroom(randomInt(width - 1), randomInt(height - 1))).toList
    val cows:List[Psyche] = (1 to cowsCount)
      .map(_ => Ooze(
        randomInt(width - 1),
        randomInt(height - 1),
        minSpeed * randomInt(speedDiff))).toList

    val creatures:List[Psyche] = List(PlayerSoul(playerInputState, width/2, height/2, 5))  ++ plants ++ cows
    new MorphoidEngine (Rect(0, 0, width, height), creatures).init()
  }
}



