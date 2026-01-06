package scripts

import scala.util.Random

/** Represents the game logic and state.
  */
class Game {
  private var game_deck = new Deck
  private var trump_suit_card = game_deck.reveal_last_card()
  private var trump_suit = trump_suit_card.get_suit()
  val player_order = new PlayerOrder
  private var attacking_player =
    player_order.get_attacking_player(trump_suit).get
  private var defending_player = player_order.get_defending_player.get

  private var attacking_cards_in_play = List[Card]()
  private var newly_added_attacking_cards = List[Card]()
  private var defending_cards_in_play = List[Card]()

  private var attacker_turn = true
  private var just_finished_defense_phase = false
  private var successful_defense = false
  private var game_initialized = false
  private var attack_count = 0
  private var finished_attacks = false

  /** Initializes the game by setting up the deck, determining the trump suit,
    * drawing hands for all players, and setting the attacking and defending
    * players.
    */
  def initializeGame(setSeed: Boolean = true): Unit =
    game_initialized = true

    player_order.reset()

    attacking_cards_in_play = List[Card]()
    defending_cards_in_play = List[Card]()

    if setSeed then Random.setSeed(42)
    game_deck.initializeDeck()
    trump_suit_card = game_deck.reveal_last_card()
    trump_suit = trump_suit_card.get_suit()

    drawHands

    attacking_player = player_order.get_attacking_player(trump_suit).get
    defending_player = player_order.get_defending_player.get
    defending_player.set_defender(true)
    
    attacker_turn = true
    just_finished_defense_phase = false
    successful_defense = false
    attack_count = 0
    finished_attacks = false

    // Set the player order to start with the attacking player
    while player_order.getHead != attacking_player do
      player_order.advance_player_order_n(1)

  def isInitialized(): Boolean =
    game_initialized

  def getNumberUndealtCards(): Int =
    game_deck.get_number_undealt_cards()

  /** Checks for a winner by returning the player whose hand is empty.
    *
    * @return
    *   An optional player whose hand is empty, or None if no player's hand is
    *   empty.
    */
  def checkForWinner: Option[String] =
    if !game_initialized then return None
    player_order
      .get_player_lineup()
      .find(player => player.hasEmptyHand())
      .map(_.get_name())

  def setStrategy(strategy: String, player: Player): String =
    player.setStrategy(strategy)

  def showStrategies(): String =
    var out = ""
    for player <- player_order.get_original_player_lineup().toList do
      out += player.showStrategy() + "\n"
    out

  /** Discards a player's hand.
    */
  def discardFirstHand: Unit =
    player_order.getHead.discardHand()

  /** Draws hands for all players.
    */
  def drawHands: Unit =
    for (a <- 0 until 6) do
      for player <- player_order.get_original_player_lineup() do
        player.draw_card(game_deck)

  /** Displays the hands of all players.
    *
    * @return
    *   A string representation of all players' hands.
    */
  def showHands: String =
    var lineup = ""
    player_order
      .get_original_player_lineup()
      .foreach(player =>
        lineup += s"${player.get_name()}'s Hand:\n${player.show_hand()}\n\n"
      )
    lineup

  def getPlayerInfo: (Player, List[Player]) =
    val temp = player_order.get_player_lineup().toList
    (temp.tail.head, temp.head :: temp.tail.tail) // defender, attackers

  def getPlayers: List[Player] =
    player_order.get_player_lineup().toList

  def getCardsInPlay: (List[Card], List[Card]) =
    (defending_cards_in_play, attacking_cards_in_play)

  /** Displays the attacking cards
    *
    * @return
    *   A string representation of the attacking cards.
    */
  def showAttackingCards: String =
    attacking_cards_in_play
      .sortBy(_.show_card())
      .foldRight("")((item, acc) => acc + item.show_card() + ", ")
      .dropRight(2) // get rid of the trailing comma

  def showDefendingCards: String =
    defending_cards_in_play
      .foldRight("")((item, acc) => acc + item.show_card() + ", ")
      .dropRight(2) // get rid of the trailing comma

  /** Displays the player order.
    *
    * @return
    *   A string representation of the player order.
    */
  def showplayer_order: String =
    player_order.showPlayerOrder

  /** Advances the player order.
    *
    * @return
    *   A string representation of the new player order.
    */
  def advancePlayerOrder: String =
    player_order.advance_player_order_n(1)
    attacking_player = player_order.getHead

    player_order.advance_player_order_n(1)
    defending_player = player_order.getHead

    player_order.advance_player_order_n(3)

  /** Performs a single move.
    */
  def doMove: String =
    // if the defense phase just finished, then reset the cards in play
    if just_finished_defense_phase then
      attacker_turn = true
      finished_attacks = false
      just_finished_defense_phase = false
      // if beginning a new round, the next attack is the first attack
      attack_count = 0

      newly_added_attacking_cards = List()

      val playersList = player_order.get_player_lineup().toList
      val attackersList = List(playersList(0), playersList(2), playersList(3))
      for attacker <- attackersList do
        // draw back up to 6 cards attacker
        while game_deck.get_number_undealt_cards() > 0 && attacker
            .getHandSize() < 6
        do attacker.draw_card(game_deck)

      // draw back up to 6 cards defender
      while game_deck.get_number_undealt_cards() > 0 && defending_player
          .getHandSize() < 6
      do defending_player.draw_card(game_deck)

      // if the defense was successful, the defender becomes the attacker
      if successful_defense then

        attacking_player = defending_player

        // set the next player as the defender
        player_order.advance_player_order_n(2)

        defending_player = player_order.getHead

        // reset order
        player_order.advance_player_order_n(3)
      else
        // the defender takes the cards in play
        attacking_cards_in_play.foreach(card =>
          defending_player.take_card(card)
        )
        defending_cards_in_play.foreach(card =>
          defending_player.take_card(card)
        )

        player_order.advance_player_order_n(2)

        attacking_player = player_order.getHead

        // set the next player as the defender
        player_order.advance_player_order_n(1)

        defending_player = player_order.getHead

        // reset order
        player_order.advance_player_order_n(3)

      attacking_cards_in_play = List[Card]()
      defending_cards_in_play = List[Card]()

    // if it's an attacker's turn, then the current player is the attacker
    
    if attacker_turn then
      if attack_count == 0 then
        // only the first attacker goes
        // execute the moves and update the attacking and defending cards in play
        val (new_attack, new_defend, good_defense) = attacking_player.executeMove(
          trump = trump_suit,
          attacking_cards_in_play = List(),
          defending_cards_in_play = defending_cards_in_play,
          is_attacker = attacker_turn,
          first_attack = (attack_count == 0),
          deck = game_deck,
          defending_player_hand_size = defending_player.getHandSize(),
          attacking_cards_all = attacking_cards_in_play
        )
        newly_added_attacking_cards = new_attack.toSet.diff(attacking_cards_in_play.toSet).toList
        attacking_cards_in_play = new_attack.toSet.toList
        attack_count += 1
        attacker_turn = false

      else        
          val (new_attack, new_defend, good_defense) = attacking_player.executeMove(
            trump = trump_suit,
            attacking_cards_in_play = attacking_cards_in_play,
            defending_cards_in_play = defending_cards_in_play,
            is_attacker = attacker_turn,
            first_attack = (attack_count == 0),
            deck = game_deck,
            defending_player_hand_size = defending_player.getHandSize(),
                      attacking_cards_all = attacking_cards_in_play

          )

          attack_count += 1
          newly_added_attacking_cards = new_attack.toSet.diff(attacking_cards_in_play.toSet).toList
                    
          attacking_cards_in_play = new_attack.toSet.toList

          attacker_turn = false

          if newly_added_attacking_cards.isEmpty then
            finished_attacks = true
            doMove

    else
      val (new_attack, new_defend, good_defense) = defending_player.executeMove(
        trump = trump_suit,
        attacking_cards_in_play = newly_added_attacking_cards,
        defending_cards_in_play = defending_cards_in_play,
        is_attacker = attacker_turn,
        first_attack = (attack_count == 0),
        deck = game_deck,
        defending_player_hand_size = defending_player.getHandSize(),
        attacking_cards_all = attacking_cards_in_play

      )

      newly_added_attacking_cards = List()

      attacking_cards_in_play = new_attack.toSet.toList
      defending_cards_in_play = new_defend.toSet.toList
      successful_defense = good_defense

      attacker_turn = true

      if (attack_count == 2) || finished_attacks || !successful_defense then
        just_finished_defense_phase = true

      if !successful_defense || finished_attacks then
        doMove


    showGameArea

  def get_trump_card(): Card = trump_suit_card

  /** Displays the game area.
    *
    * @return
    *   A string representation of the game area.
    */
  def showGameArea: String =
    val expectedResult =
      "Number of Undealt Cards:\n" +
        s"${game_deck.get_number_undealt_cards()}\n" +
        "Trump Suit Card:\n" +
        s"${trump_suit_card.show_card()}\n" +
        "\n" +
        "Attacking Player:\n" +
        s"${attacking_player.get_name()}\n" +
        "Attacking Card(s) In Play:\n" +
        showAttackingCards +
        "\n" +
        "\n" +
        "Defending Player:\n" +
        s"${defending_player.get_name()}\n" +
        "Defending Card(s) In Play:\n" +
        showDefendingCards +
        "\n" +
        "\n" +
        showHands +
        "\n" +
        "Winner:" + checkForWinner.getOrElse("") + "\n" +
        "\n" +
        "Loser\n" +
        "\n" +
        "\n"
    expectedResult
}
