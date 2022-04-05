package de.htwg.se.uno.model.gameComponent.gameBaseImpl

import com.fasterxml.jackson.annotation.JsonValue
import com.google.inject.Inject
import com.google.inject.name.Named
import de.htwg.se.uno.model.gameComponent.GameInterface
import scala.util.Random
import scala.annotation.tailrec

case class Game @Inject() (
    @Named("DefaultPlayers") override val numOfPlayers: 2 | 3 | 4,
    override val coveredCards: List[Card] = List(),
    override val revealedCards: List[Card] = List(),
    override val player: Player = Player(List()),
    override val enemies: List[Enemy] = List(),
    override val revealedCardEffect: Int = 0,
    override val activePlayer: Int = 0,
    override val direction: Boolean = true,
    override val alreadyPulled: Boolean = false
) extends GameInterface(numOfPlayers, coveredCards, revealedCards, player, enemies, revealedCardEffect, activePlayer, direction, alreadyPulled):
  
  def copyGame(numOfPlayers: 2 | 3 | 4 = numOfPlayers, coveredCards: List[Card] = coveredCards, revealedCards: List[Card] = revealedCards,
               player: Player = player, enemies: List[Enemy] = enemies, revealedCardEffect: Int = revealedCardEffect,
               activePlayer: Int = activePlayer, direction: Boolean = direction, alreadyPulled: Boolean = alreadyPulled): Game =
    copy(numOfPlayers, coveredCards, revealedCards, player, enemies, revealedCardEffect, activePlayer, direction, alreadyPulled)

  def createGame(): Game =
    val cards = CardStack().createCoveredCardStack().shuffle().cardStack
    val revealedCardsNew = List(cards.head)
    @tailrec
    def initializePlayer(i: Int, handCards: List[Card]): List[Card] =
      if i < 7 then initializePlayer(i + 1, cards(i + 1) :: handCards) else handCards
    val playerNew = Player(initializePlayer(0, List[Card]()))
    @tailrec
    def initializeEnemies(enemyCounter: Int, i: Int, enemiesTemp: List[Card]): List[Card] =
      if enemyCounter < numOfPlayers - 1 then
        if i < 7 then
          initializeEnemies(enemyCounter, i + 1, cards((enemyCounter + 1) * 7 + 1 + i) :: enemiesTemp)
        else
          initializeEnemies(enemyCounter + 1, 0, enemiesTemp)
      else enemiesTemp
    val enemiesCards = initializeEnemies(0, 0, List[Card]())
    val enemyOne = enemiesCards.take(7)
    val enemyTwo = if numOfPlayers < 3 then List()
      else if numOfPlayers == 3 then enemiesCards.drop(7)
      else enemiesCards.slice(7, 14)
    val enemyThree = if numOfPlayers < 4 then List() else enemiesCards.drop(14)
    val enemiesNew = List(Enemy(enemyOne), Enemy(enemyTwo), Enemy(enemyThree))
    val coveredCardsNew = cards.drop(numOfPlayers * 7 + 1)
    copy(
      numOfPlayers,
      coveredCardsNew,
      revealedCardsNew,
      playerNew,
      enemiesNew,
      revealedCardEffect = 0,
      activePlayer = numOfPlayers - 1,
      direction = true,
      alreadyPulled = false
    )

  def createTestGame(): Game =
    val cards = CardStack().createCoveredCardStack().cardStack
    val revealedCardsNew = List(cards.head)
    val playerNew = Player(List(cards(100), cards(104), cards(1), cards(5), cards(32), cards(61), cards(19), cards(21), cards(23)))
    val enemiesNew = List(Enemy(List(cards(101), cards(105), cards(2), cards(6), cards(33), cards(62), cards(20), cards(22), cards(24))),
      Enemy(List(cards(102), cards(106), cards(3), cards(7), cards(34), cards(63), cards(44), cards(46), cards(48))),
      Enemy(List(cards(103), cards(107), cards(4), cards(8), cards(35), cards(64), cards(45), cards(47), cards(49))))
    val coveredCardsNew = ((((cards filterNot revealedCards.contains) filterNot player.handCards.contains) filterNot
      enemies.head.enemyCards.contains) filterNot enemies(1).enemyCards.contains) filterNot enemies(2).enemyCards.contains
    copy(
      coveredCards = coveredCardsNew,
      revealedCards = revealedCardsNew,
      player = playerNew,
      enemies = enemiesNew,
      revealedCardEffect = 0,
      activePlayer = numOfPlayers - 1,
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
            enemyPull(enemyIndex).copy(alreadyPulled = false, revealedCardEffect = 0)
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
    if revealedCardEffect != -1 then
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
    else
      changeActivePlayer().copy(revealedCardEffect = 0)

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

  def addCardToList(list: Int, card: Card): Game =
    list match
      case 0 | 1 | 2 => copy(enemies = enemies.updated(list, enemies(list).pullCard(card)))
      case 3 => copy(revealedCards = card :: revealedCards)
      case 4 => copy(player = player.pullCard(card))
      case _ => copy(coveredCards = card :: coveredCards)

  def reverseList(list: Int): Game =
    list match
      case 0 | 1 | 2 => copy(enemies = enemies.updated(list, Enemy(enemies(list).enemyCards.reverse)))
      case 3 => copy(revealedCards = revealedCards.reverse)
      case 4 => copy(player = Player(player.handCards.reverse))
      case _ => copy(coveredCards = coveredCards.reverse)

  def shuffle(): Game = copy(coveredCards = Random.shuffle(coveredCards ::: revealedCards.tail), revealedCards = List(revealedCards.head))

  override def toString: String =
    val cardTop = "┌-------┐  "
    val cardEmpty = "|       |  "
    val coveredCard = "|  Uno  |  "
    val cardBottom = "└-------┘  "
    @tailrec
    def enemy1CardsRecursion(i: Int, enemyList: List[String]): List[String] =
      if i < enemies.head.enemyCards.length then
        enemy1CardsRecursion(i + 1, List(enemyList.head + cardTop, enemyList(1) + cardEmpty, enemyList(2) + coveredCard, enemyList(3) + cardBottom))
      else
        enemyList
    val enemy1Cards = enemy1CardsRecursion(0, List("", "", "", ""))
    @tailrec
    def playerCardsRecursion(i: Int, playerList: List[String]): List[String] =
      if i < player.handCards.length then
        playerCardsRecursion(i + 1, List(playerList.head + cardTop, playerList(1) + cardEmpty, playerList(2) + "|  " + player.handCards(i).toString + "  |  ", playerList(3) + cardBottom))
      else
        playerList
    val playerCards = playerCardsRecursion(0, List("", "", "", ""))
    val centerCardsTop = cardTop + "           ┌-------┐" + "\n"
    val centerCardsEmpty = cardEmpty + "           |       |" + "\n"
    val centerCardsText = coveredCard + "           |  " + revealedCards.head.toString + "  |" + "\n"
    val centerCardsBottom = cardBottom + "           └-------┘" + "\n"
    val enemy2Cards = if numOfPlayers >= 3 then
      @tailrec
      def enemy2CardsRecursion(i: Int, enemyList: List[String]): List[String] =
        if i < enemies(1).enemyCards.length then
          enemy2CardsRecursion(i + 1, List(enemyList.head + cardTop, enemyList(1) + cardEmpty, enemyList(2) + coveredCard, enemyList(3) + cardBottom))
        else
          enemyList
      enemy2CardsRecursion(0, List("", "", "", ""))
    else
      List("", "", "", "")
    val enemy3Cards = if numOfPlayers >= 4 then
      @tailrec
      def enemy3CardsRecursion(i: Int, enemyList: List[String]): List[String] =
        if i < enemies(2).enemyCards.length then
          enemy3CardsRecursion(i + 1, List(enemyList.head + cardTop, enemyList(1) + cardEmpty, enemyList(2) + coveredCard, enemyList(3) + cardBottom))
        else
          enemyList
      enemy3CardsRecursion(0, List("", "", "", ""))
    else
      List("", "", "", "")
    enemy1Cards.head + "\t\t\t\t\t" + enemy2Cards.head + "\n" +
      enemy1Cards(1) + "\t\t\t\t\t" + enemy2Cards(1) + "\n" +
      enemy1Cards(2) + "\t\t\t\t\t" + enemy2Cards(2) + "\n" +
      enemy1Cards(1) + "\t\t\t\t\t" + enemy2Cards(1) + "\n" +
      enemy1Cards(3) + "\t\t\t\t\t" + enemy2Cards(3) + "\n\n" +
      centerCardsTop + centerCardsEmpty + centerCardsText + centerCardsEmpty + centerCardsBottom +
      playerCards.head + "\t\t\t\t\t" + enemy3Cards.head + "\n" +
      playerCards(1) + "\t\t\t\t\t" + enemy3Cards(1) + "\n" +
      playerCards(2) + "\t\t\t\t\t" + enemy3Cards(2) + "\n" +
      playerCards(1) + "\t\t\t\t\t" + enemy3Cards(1) + "\n" +
      playerCards(3) + "\t\t\t\t\t" + enemy3Cards(3)