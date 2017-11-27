package org.kirhgoff.morphoid.engine

import org.kirhgoff.morphoid.PlayerInputState
import org.kirhgoff.morphoid.engine.Dice._

import scala.collection.{JavaConverters, mutable}

abstract class GameEvent(sourceId:String, targetId:String)
case class CreatureMoves(sourceId:String, targetId:String, direction:Direction) extends GameEvent(sourceId, targetId)
case class CreatureAttacks(sourceId:String, targetId:String, direction:Direction) extends GameEvent(sourceId, targetId)
case class CreatureObserve(sourceId:String, targetId:String, surroundings:List[Physical]) extends GameEvent(sourceId, targetId)

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
  //val GOD_ENGINE = "GOD ENGINE v.01" //-> prototype
  val GOD_ENGINE = "GOD ENGINE v.03" //-> multi-cell organisms

  //private val player = initialEntities.head
  private val creatures =  mutable.Map[String, Creature](initialEntities map (p => p.id -> p.creature): _*)
  private val souls = mutable.Map[String, Psyche](initialEntities map (p => p.id -> p): _*)
  private val decoy =  mutable.Map[String, Decoy]() //TODO optimize - memory leak

  private var energyBalanceController = EnergyBalanceController.generic()

  def setEnergyBalanceController(controller:EnergyBalanceController): Unit = {
    energyBalanceController = controller
  }

  //TODO move to test where it is used
  //TODO make decoy creature + add Alive interface with energy
  def fullEnergy = creatures.values.map(_.energy).sum + decoy.values.map(_.energy).sum

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

  def getDecoy:List[Decoy] = decoy.values.toList
  def getDecoyJava = JavaConverters.asJavaCollection(getDecoy)

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
    case _ => -666 // To easily notice if forgot to add
  }

  private def addEntity(psyche: Psyche) = {
    val creature = psyche.creature
    creatures(creature.id) = creature
    souls(psyche.id) = psyche
    psyche.setEngine(this)
  }

  def createDecoy(creature: Creature):Decoy = new Decoy(creature.energy)

  def tick() = {
    //println(s"MEngine.tick() ${Dice.nextTickNumber} begin: $souls")
    val (dead,alive) = souls.values
      .partition(_.creature.energy <= energyBalanceController.decoyThreshold)

    // Turn dead to decay
    dead.foreach(psyche => {
      val newDecoy = createDecoy(psyche.creature)
      val creatureId = psyche.creature.id
      souls.remove(psyche.id)
      creatures.remove(creatureId )
      decoy.put(creatureId , newDecoy)
    })

    alive
      .filter(_.readyToAct)
      .foreach(p => {
        val sur = surroundings(p.creature, p.sight)
        // TODO understand how act works with multicellulars
        val batch = p.act(sur)
        //      println(s"creature ${p.creature}" +
        //        s"\n\tsurr=${sur.map(s => str(s)).mkString(" ")}" +
        //        s"\n\tbatch=$batch")
        execute(validate(batch))
    })

    //TODO ask Michal how to rewrite functionally
    creatures.values.foreach(creature => {
      creature.updateEnergy(creature.cells.foldLeft(0.0)(
        (a, c) => a + energyGrowth(cellType(c))
      ))
    })

    decoy.foreach { case (id, item) => {
      item.updateEnergy(energyBalanceController.decoyDecay)
      if (item.energy <= energyBalanceController.completeDecoy)
        decoy.remove(id)
    }}

    //println(s"MEngine.tick() end: $souls $decoy")
    this
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
        val intersectsWithSomething = creatures.values.filter(creature != _).map(_.boundingRect).exists(_.intersects(rect))
        val insideBorders = levelRect.includes(rect)
        //println(s"Validating creature $creatures(id) direction=$direction iws=$intersectsWithSomething ib=$insideBorders")
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
        val creature = creatures(id)
        //println(s"execute creatureMoves=$creature direction=$direction")

        unregisterCreature(creature)
        registerCreature(creature.move(direction))
      }
      case CreatureAttacks(_, id, direction) => {
        val creature = creatures(id)
        creature.attack(direction)
        val newId = Dice.makeId("pew")
        val projectileOrigin = creature.origin.nextTo(direction)
        val projectile = new Creature(newId, "projectile", Map(projectileOrigin -> new Seed))
        addEntity(new Projectile(newId, direction, 5, projectile))
      }
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


  def createSimple(width:Int, height:Int, playerInputState: PlayerInputState) = new MorphoidEngine (
    Rect(0, 0, width, height),
    List(
      PlayerSoul(playerInputState, 10, 10, 5),
      Ooze(3, 5, 40),
      Ooze(15, 15, 60),
      Ooze(9, 11, 50),
      Shroom(1, 8),
      Shroom(6, 13),
      Shroom(16, 12)
    )
  ).init()

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



