package model.gameComponent.gameBaseImpl

import com.fasterxml.jackson.annotation.JsonValue
import com.google.inject.Inject
import com.google.inject.name.Named
import model.gameComponent.GameInterface
import play.api.libs.json.{JsValue, Json, JsNumber, JsBoolean, JsArray, JsString}

import scala.util.Random
import scala.annotation.tailrec

case class Game (
    override val numOfPlayers: 2 | 3 | 4 = 2,
    override val coveredCards: List[Card] = List(),
    override val revealedCards: List[Card] = List(),
    override val player: Player = Player(List()),
    override val enemies: List[Enemy] = List(),
    override val revealedCardEffect: Int = 0,
    override val activePlayer: Int = 0,
    override val direction: Boolean = true,
    override val alreadyPulled: Boolean = false
) extends GameInterface(numOfPlayers, coveredCards, revealedCards, player, enemies, revealedCardEffect, activePlayer, direction, alreadyPulled):

  def createGame(gameSize: 2 | 3 | 4): Game =
    val cards = CardStack().createCoveredCardStack().shuffle().cardStack
    val revealedCardsNew = List(cards.head)
    @tailrec
    def initializePlayer(i: Int, handCards: List[Card]): List[Card] =
      if i < 7 then initializePlayer(i + 1, cards(i + 1) :: handCards) else handCards
    val playerNew = Player(initializePlayer(0, List[Card]()))
    @tailrec
    def initializeEnemies(enemyCounter: Int, i: Int, enemiesTemp: List[Card]): List[Card] =
      if enemyCounter < gameSize - 1 then
        if i < 7 then
          initializeEnemies(enemyCounter, i + 1, cards((enemyCounter + 1) * 7 + 1 + i) :: enemiesTemp)
        else
          initializeEnemies(enemyCounter + 1, 0, enemiesTemp)
      else enemiesTemp
    val enemiesCards = initializeEnemies(0, 0, List[Card]())
    val enemyOne = enemiesCards.take(7)
    val enemyTwo = if gameSize < 3 then List()
      else if gameSize == 3 then enemiesCards.drop(7)
      else enemiesCards.slice(7, 14)
    val enemyThree = if gameSize < 4 then List() else enemiesCards.drop(14)
    val enemiesNew = List(Enemy(enemyOne), Enemy(enemyTwo), Enemy(enemyThree))
    val coveredCardsNew = cards.drop(gameSize * 7 + 1)
    copy(
      numOfPlayers = gameSize,
      coveredCardsNew,
      revealedCardsNew,
      playerNew,
      enemiesNew,
      revealedCardEffect = 0,
      activePlayer = gameSize - 1,
      direction = true,
      alreadyPulled = false
    )

  def pushMove(string: String, color: Int): Game =
    if revealedCardEffect != -1 then
      val cardOption = player.findCard(string)
      if cardOption.isDefined then
        val card = cardOption.get
        if player.canPush(card, revealedCards.head, revealedCardEffect) then
          val coloredCard = color match
            case 1 => Card(Color.Blue, card.value)
            case 2 => Card(Color.Green, card.value)
            case 3 => Card(Color.Yellow, card.value)
            case 4 => Card(Color.Red, card.value)
            case _ => card
          val revealedCardsNew = coloredCard :: revealedCards
          val alreadyPulledNew = false
          copy(
            revealedCards = revealedCardsNew,
            player = player.pushCard(card),
            revealedCardEffect = discoverRevealedCardEffect(card),
            direction = if card.value == Value.DirectionChange then !direction else direction,
            alreadyPulled = alreadyPulledNew
          )
        else copy()
      else copy()
    else changeActivePlayer().copy(revealedCardEffect = 0)

  def pullMove(): Game =
    if revealedCardEffect != -1 then
      if !alreadyPulled then
        @tailrec
        def pullMoveRecursion(i: Int, playerTemp: Player, coveredCardsTemp: List[Card]): Game =
          if i < revealedCardEffect then
            pullMoveRecursion(i + 1, playerTemp.pullCard(coveredCardsTemp.head), coveredCardsTemp.tail)
          else if revealedCardEffect == 0 && i == 0 then
            copy(player = playerTemp.pullCard(coveredCardsTemp.head), coveredCards = coveredCardsTemp.tail, alreadyPulled = true)
          else
            copy(player = playerTemp, coveredCards = coveredCardsTemp, alreadyPulled = false)
        pullMoveRecursion(0, player, coveredCards).copy(revealedCardEffect = 0)
      else copy(alreadyPulled = false, revealedCardEffect = 0)
    else changeActivePlayer().copy(revealedCardEffect = 0)

  def enemy(enemyIndex: Int, kiNeeded: Boolean = true): Game =
    if revealedCardEffect != -1 then
      if ((nextTurn() && player.handCards.length <= 3) || (!nextTurn() && enemies(nextEnemy() - 1).enemyCards.length <= 3)) && kiNeeded then
        ki(enemyIndex)
      else
        @tailrec
        def enemyRecursion(i: Int, gameTemp: Game, canPush: (Card, Card, Int) => Boolean): Game =
          val enemy = enemies(enemyIndex)
          if i < enemy.enemyCards.length && !alreadyPulled then
            val card = enemy.enemyCards(i)
            if canPush(card, revealedCards.head, revealedCardEffect) then
              enemyPush(card, enemyIndex)
            else
              enemyRecursion(i + 1, gameTemp, canPush)
          else gameTemp
        val canPushFunctions = List(
          enemies(enemyIndex).shouldPushDrawCard,
          enemies(enemyIndex).canPushBasicCardWithSameColor,
          enemies(enemyIndex).canPushSuspendOrDirectionChangeWithSameColor,
          enemies(enemyIndex).canPushBasicCardWithSameValue,
          enemies(enemyIndex).canPushSuspendOrDirectionChangeWithSameValue,
          enemies(enemyIndex).canPushColorChange,
          enemies(enemyIndex).canPushOnSpecial,
          enemies(enemyIndex).canPushPlusTwo,
          enemies(enemyIndex).canPushPlusFour
        )
        @tailrec
        def canPushRecursion(i: Int): Game =
          if i < 6 || (i < canPushFunctions.length && enemies(enemyIndex).enemyCards.length < 4) then
            val newGame = enemyRecursion(0, copy(), canPushFunctions(i))
            if newGame.revealedCards.length != revealedCards.length then
              newGame
            else
              canPushRecursion(i + 1)
          else if !alreadyPulled then
            enemyPull(enemyIndex)
          else
            copy(alreadyPulled = false)
        canPushRecursion(0)
    else
      copy(revealedCardEffect = 0)

  def ki(enemyIndex: Int): Game =
    @tailrec
    def kiRecursion1(i: Int): Int =
      if i < enemies(enemyIndex).enemyCards.length then
        if enemies(enemyIndex).canPushPlusFour(enemies(enemyIndex).enemyCards(i), revealedCards.head, revealedCardEffect) then
          i
        else
          kiRecursion1(i + 1)
      else
        -1
    @tailrec
    def kiRecursion2(i: Int): Int =
      if i < enemies(enemyIndex).enemyCards.length then
        if enemies(enemyIndex).canPushPlusTwo(enemies(enemyIndex).enemyCards(i), revealedCards.head, revealedCardEffect) then
          i
        else
          kiRecursion2(i + 1)
      else
        -1
    @tailrec
    def kiRecursion3(i: Int): Int =
      if i < enemies(enemyIndex).enemyCards.length then
        if (enemies(enemyIndex).enemyCards(i).value == Value.Suspend || enemies(enemyIndex).enemyCards(i).value == Value.DirectionChange)
          && revealedCardEffect <= 0 && (enemies(enemyIndex).enemyCards(i).value == revealedCards.head.value ||
          enemies(enemyIndex).enemyCards(i).color == revealedCards.head.color)
        then
          i
        else
          kiRecursion3(i + 1)
      else
        -1
    val index = if kiRecursion1(0) != -1 then kiRecursion1(0)
      else if kiRecursion2(0) != -1 then kiRecursion2(0)
      else kiRecursion3(0)
    if index != -1 then
      enemyPush(enemies(enemyIndex).enemyCards(index), enemyIndex)
    else
      enemy(enemyIndex, false)

  def enemyPush(card: Card, enemyIndex: Int): Game =
    @tailrec
    def enemyPushRecursion(i: Int, maxColorCounter: Int, color: Color): Color =
      if i < 4 then
        @tailrec
        def enemyPushInnerRecursion(j: Int, colorCounter: Int): Int =
          if j < enemies(enemyIndex).enemyCards.length then
            if enemies(enemyIndex).enemyCards(j).color == Color.fromOrdinal(i) then
              enemyPushInnerRecursion(j + 1, colorCounter + 1)
            else
              enemyPushInnerRecursion(j + 1, colorCounter)
          else colorCounter
        val colorCounter = enemyPushInnerRecursion(0, 0)
        if colorCounter > maxColorCounter then
          enemyPushRecursion(i + 1, colorCounter, Color.fromOrdinal(i))
        else
          enemyPushRecursion(i + 1, maxColorCounter, color)
      else
        color
    val coloredCard = if card.color == Color.Special then
      Card(enemyPushRecursion(0, 0, Color.fromOrdinal(0)), card.value)
    else
      card
    copy(
      revealedCards = coloredCard :: revealedCards,
      revealedCardEffect = discoverRevealedCardEffect(card),
      direction = if card.value == Value.DirectionChange then !direction else direction,
      alreadyPulled = false,
      enemies = enemies.updated(enemyIndex, enemies(enemyIndex).pushCard(card))
    )

  def enemyPull(enemyIndex: Int): Game =
    if !alreadyPulled then
      @tailrec
      def enemyPullRecursion(i: Int, enemyTemp: Enemy, coveredCardsTemp: List[Card]): Game =
        if i < revealedCardEffect then
          enemyPullRecursion(i + 1, enemyTemp.pullCard(coveredCardsTemp.head), coveredCardsTemp.tail)
        else if revealedCardEffect == 0 && i == 0 then
          copy(
            enemies = enemies.updated(enemyIndex, enemyTemp.pullCard(coveredCardsTemp.head)),
            coveredCards = coveredCardsTemp.tail,
            alreadyPulled = true
          )
        else
          copy(enemies = enemies.updated(enemyIndex, enemyTemp), coveredCards = coveredCardsTemp, alreadyPulled = false)
      enemyPullRecursion(0, enemies(enemyIndex), coveredCards).copy(revealedCardEffect = 0)
    else
      copy(alreadyPulled = false, revealedCardEffect = 0)

  def discoverRevealedCardEffect(card: Card): Int =
    card.value match
      case Value.Suspend  => -1
      case Value.PlusTwo  => revealedCardEffect + 2
      case Value.PlusFour => revealedCardEffect + 4
      case _              => 0

  def nextEnemy(): Int =
    if numOfPlayers == 2 || (activePlayer == 0 && direction) || (activePlayer == 2 && !direction) then
      1
    else if (numOfPlayers >= 3 && activePlayer == 1 && direction) || (numOfPlayers == 3 && activePlayer == 0 && !direction) || (activePlayer == 3 && !direction) then
      2
    else
      3

  def nextTurn(): Boolean =
    (activePlayer == 1 && (!direction || numOfPlayers == 2)) || (activePlayer == 2 && direction && numOfPlayers == 3) || (activePlayer == 3 && direction && numOfPlayers == 4)

  def changeActivePlayer(): Game = if nextTurn() then copy(activePlayer = 0) else copy(activePlayer = nextEnemy())

  def shuffle(): Game = copy(coveredCards = Random.shuffle(coveredCards ::: revealedCards.tail), revealedCards = List(revealedCards.head))

  override def toString: String =
    val cardTop = "┌-------┐  "
    val cardEmpty = "|       |  "
    val coveredCard = "|  Uno  |  "
    val cardBottom = "└-------┘  "
    val centerCardsTop = cardTop + "           ┌-------┐\n"
    val centerCardsEmpty = cardEmpty + "           |       |\n"
    val centerCardsText = coveredCard + "           |  " + revealedCards.head.toString + "  |\n"
    val centerCardsBottom = cardBottom + "           └-------┘\n"
    val spaces = "\t\t\t\t\t"

    (StringBuilder()
      ++= (for _ <- enemies.head.enemyCards.indices yield cardTop).mkString("") ++= spaces ++= (for _ <- enemies(1).enemyCards.indices yield cardTop).mkString("") ++= "\n"
      ++= (for _ <- enemies.head.enemyCards.indices yield cardEmpty).mkString("") ++= spaces ++= (for _ <- enemies(1).enemyCards.indices yield cardEmpty).mkString("") ++= "\n"
      ++= (for _ <- enemies.head.enemyCards.indices yield coveredCard).mkString("") ++= spaces ++= (for _ <- enemies(1).enemyCards.indices yield coveredCard).mkString("") ++= "\n"
      ++= (for _ <- enemies.head.enemyCards.indices yield cardEmpty).mkString("") ++= spaces ++= (for _ <- enemies(1).enemyCards.indices yield cardEmpty).mkString("") ++= "\n"
      ++= (for _ <- enemies.head.enemyCards.indices yield cardBottom).mkString("") ++= spaces ++= (for _ <- enemies(1).enemyCards.indices yield cardBottom).mkString("") ++= "\n"
      ++= centerCardsTop ++= centerCardsEmpty ++= centerCardsText ++= centerCardsEmpty ++= centerCardsBottom
      ++= (for _ <- player.handCards.indices yield cardTop).mkString("") ++= spaces ++= (for _ <- enemies(2).enemyCards.indices yield cardTop).mkString("") ++= "\n"
      ++= (for _ <- player.handCards.indices yield cardEmpty).mkString("") ++= spaces ++= (for _ <- enemies(2).enemyCards.indices yield cardEmpty).mkString("") ++= "\n"
      ++= player.handCards.map(card => "|  " + card + "  |  ").mkString("") ++= spaces ++= (for _ <- enemies(2).enemyCards.indices yield coveredCard).mkString("") ++= "\n"
      ++= (for _ <- player.handCards.indices yield cardEmpty).mkString("") ++= spaces ++= (for _ <- enemies(2).enemyCards.indices yield cardEmpty).mkString("") ++= "\n"
      ++= (for _ <- player.handCards.indices yield cardBottom).mkString("") ++= spaces ++= (for _ <- enemies(2).enemyCards.indices yield cardBottom).mkString("")
      ).toString

  def gameToJson(): String =
    Json.obj(
      "game" -> Json.obj(
        "numOfPlayers" -> JsNumber(numOfPlayers),
        "activePlayer" -> JsNumber(activePlayer),
        "direction" -> JsBoolean(direction),
        "anotherPull" -> JsBoolean(alreadyPulled),
        "specialCard" -> JsNumber(revealedCardEffect),
        "enemy1Cards" -> JsArray(for cardNumber <- enemies.head.enemyCards.indices yield JsString(enemies.head.enemyCards(cardNumber).toString)),
        "enemy2Cards" -> JsArray(for cardNumber <- enemies(1).enemyCards.indices yield JsString(enemies(1).enemyCards(cardNumber).toString)),
        "enemy3Cards" -> JsArray(for cardNumber <- enemies(2).enemyCards.indices yield JsString(enemies(2).enemyCards(cardNumber).toString)),
        "openCardStack" -> JsArray(for cardNumber <- revealedCards.indices yield JsString(revealedCards(cardNumber).toString)),
        "playerCards" -> JsArray(for cardNumber <- player.handCards.indices yield JsString(player.handCards(cardNumber).toString)),
        "coveredCardStack" -> JsArray(for cardNumber <- coveredCards.indices yield JsString(coveredCards(cardNumber).toString))
      )
    ).toString()

  def gameFromJson(input: String): Game =
    val json: JsValue = Json.parse(input)
    
    val numOfPlayersNew: 2 | 3 | 4 =
      (json \ "game" \ "numOfPlayers").get.toString.toInt match
        case 2 => 2
        case 3 => 3
        case 4 => 4
        case _ => 2
    val activePlayerNew = (json \ "game" \ "activePlayer").get.toString.toInt
    val directionNew = (json \ "game" \ "direction").get.toString.toBoolean
    val alreadyPulledNew = (json \ "game" \ "anotherPull").get.toString.toBoolean
    val revealedCardEffectNew = (json \ "game" \ "specialCard").get.toString.toInt
    val cards = CardStack().createCoveredCardStack(1, 1).addColoredSpecialCards().cardStack

    def createCardList(listName: String): List[Card] =
      (json \ "game" \ listName).as[List[String]].flatMap(cardString => cards.filter(card => card.toString.equals(cardString)))

    copy(
      numOfPlayers = numOfPlayersNew,
      activePlayer = activePlayerNew,
      direction = directionNew,
      alreadyPulled = alreadyPulledNew,
      revealedCardEffect = revealedCardEffectNew,
      enemies = List(
        Enemy(createCardList("enemy1Cards")),
        Enemy(createCardList("enemy2Cards")),
        Enemy(createCardList("enemy3Cards")),
      ),
      player = Player(createCardList("playerCards")),
      coveredCards = createCardList("coveredCardStack"),
      revealedCards = createCardList("openCardStack")
    )