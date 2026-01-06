package scripts

import scala.util.Random
import scala.util.control.Breaks._
import scala.compiletime.ops.double

/** Represents a player in the game.
  *
  * @param name
  *   The name of the player.
  */
class Player(name: String) {
  private var hand = Hand()
  private var strategy = "default"
  private var is_defender = false

  /** Retrieves the player's name.
    *
    * @return
    *   The name of the player.
    */
  def get_name(): String =
    name

  /** Sets the player as the defender.
    */
  def set_defender(is_def: Boolean): Unit =
    is_defender = is_def

  /** Displays the player's hand.
    *
    * @return
    *   A string representation of the player's hand.
    */
  def show_hand(): String =
    hand.show_cards()

  def get_hand(): Hand =
    hand

  /** Draws a card from the deck and adds it to the player's hand
    */
  def draw_card(deck: Deck): Unit =
    hand.add_card(deck.draw_card())

  /** Draws a card from the deck and adds it to the player's hand
    */
  def take_card(card: Card): Unit =
    hand.add_card(card)

  /** Checks if the player's hand is valid.
    *
    * @return
    *   True if the hand is valid, otherwise false.
    */
  def hasValidHand(): Boolean =
    hand.is_valid_hand()

  /** Checks if the player's hand is empty.
    *
    * @return
    *   True if the hand is empty, otherwise false.
    */
  def hasEmptyHand(): Boolean =
    hand.isEmpty()

  /** Counts the number of cards in the player's hand.
    *
    * @return
    *   True if the hand is empty, otherwise false.
    */
  def getHandSize(): Int =
    hand.get_cards().length

  /** Retrieves the lowest trump card in the player's hand.
    *
    * @param trump
    *   The trump suit to filter the cards.
    * @return
    *   An optional card, if a valid trump card exists.
    */
  def get_lowest_trump(trump: String): Option[Card] =
    hand.get_lowest_trump(trump)

  /** Plays a specified card from the player's hand.
    *
    * @param card
    *   The card to be played.
    */
  def play_card(card: Card): Unit = {
    hand.discard_card(card)
  }

  /** Discards the player's hand.
    */
  def discardHand(): Unit =
    hand.discardHand()

  /** Executes a move based on the player's strategy. \@ return A tuple of the
    * attacking and defending cards in play.
    */
  def executeMove(
      trump: String,
      attacking_cards_in_play: List[Card],
      defending_cards_in_play: List[Card],
      is_attacker: Boolean,
      first_attack: Boolean = false,
      deck: Deck,
      defending_player_hand_size: Int = 6,
      attacking_cards_all: List[Card]
  ): (List[Card], List[Card], Boolean) = {
    // println(s"Player: ${name} is playing with strategy: ${strategy}")
    strategy match
      case "default" =>
        if !is_attacker then // if it's the defender

          // in the default strategy, the player only plays the
          // lowest-ranked card in their hand when the player is the defender
          // If they cannot defend, he will take the attacking card
          var played_cards: List[Card] = List()
          var defense_counter = 0

          // the defender has to defend against each attacking card in play
          for attack_card <- attacking_cards_in_play do

            // the defender can respond with any card of the same suit as the attacking card, or trump
            val allowed_cards_by_suit =
              hand.get_cards_by_suits(Set(attack_card.get_suit(), trump))

            // the defending card played must be greater than the attacking card
            val allowed_cards_by_rank = allowed_cards_by_suit.filter(
              _.get_n_rank(trump) > attack_card.get_n_rank(trump)
            )

            // if the defender has a card that can beat the attacking card, they play the lowest one
            if allowed_cards_by_rank.nonEmpty then
              val card_to_play =
                allowed_cards_by_rank.minBy(_.get_n_rank(trump))

              play_card(card_to_play)
              played_cards = card_to_play :: played_cards
              defense_counter += 1

          // if the player cannot defend, they take the attacking cards
          if defense_counter < attacking_cards_in_play.length then
            attacking_cards_all.foreach(card => hand.add_card(card))
            defending_cards_in_play.foreach(card => hand.add_card(card))
            return (List(), List(), false)

          // if the player can defend, they play those cards
          // println(s"played cards: ${played_cards.map(_.show_card())}")
          return (
            attacking_cards_all,
            played_cards ::: defending_cards_in_play,
            true
          )
        else // if it's the attacker
          if !first_attack then // do nothing on subsequent attacks
            return (attacking_cards_in_play, defending_cards_in_play, true)
          // in the default strategy, the player only ever plays
          // the lowest-ranked card in their hand when the player is the attacker

          // next, get all cards of the lowest rank
          if hand.get_cards().isEmpty then 
            return (attacking_cards_all, defending_cards_in_play, true)
          val lowest_rank =
            hand.get_cards().minBy(_.get_n_rank(trump)).get_n_rank(trump)
          val cards_to_play =
            hand.get_cards().filter(_.get_n_rank(trump) == lowest_rank)

          cards_to_play.foreach(play_card)
          return (
            cards_to_play ::: attacking_cards_all,
            defending_cards_in_play,
            true
          )
      case "trump hoarder" =>
        if !is_attacker then
          if deck.get_number_undealt_cards() != 0 then
            // in the trump hoarder strategy, the player only plays the
            // lowest-ranked card in their hand when the player is the defender
            // If they need to use a trump card, they will take the attacking cards
            // and forfeit their turn. Additionally, if attacked with a trump card
            // they will take the cards and forfeit their turn.
            // this only happens when the draw pile is nonEmpty
            // If they cannot defend, he will take the attacking card
            var played_cards: List[Card] = List()
            var defense_counter = 0

            // the defender has to defend against each attacking card in play
            // responding with
            breakable {
              for attack_card <- attacking_cards_in_play do
                if attack_card.is_trump(trump) then
                  defense_counter = 0
                  break

                // the defender can respond with any card of the same suit as the attacking card, or trump
                val allowed_cards_by_suit =
                  hand.get_cards_by_suits(Set(attack_card.get_suit(), trump))

                // the defending card played must be greater than the attacking card
                val allowed_cards_by_rank = allowed_cards_by_suit.filter(
                  _.get_n_rank(trump) > attack_card.get_n_rank(trump))

                // if the defender has a card that can beat the attacking card, they play the lowest one
                if allowed_cards_by_rank.nonEmpty then
                  val card_to_play =
                    allowed_cards_by_rank.minBy(_.get_n_rank(trump))

                  if card_to_play.is_trump(trump) then
                    defense_counter = 0
                    break

                  play_card(card_to_play)
                  played_cards = card_to_play :: played_cards
                  defense_counter += 1
            }

            // if the player cannot defend, they take the attacking cards
            if defense_counter < attacking_cards_in_play.length then
              attacking_cards_all.foreach(card => hand.add_card(card))
              defending_cards_in_play.foreach(card => hand.add_card(card))

              return (List(), List(), false)

            // if the player can defend, they play those cards
            return (
              attacking_cards_all,
              played_cards ::: defending_cards_in_play,
              true
              )

          else
            var played_cards: List[Card] = List()
            var defense_counter = 0

            // the defender has to defend against each attacking card in play
            // responding with
            for attack_card <- attacking_cards_in_play do
              // println(s"attack card: ${attack_card.show_card()}")
              // the defender can respond with any card of the same suit as the attacking card, or trump
              val allowed_cards_by_suit =
                hand.get_cards_by_suits(Set(attack_card.get_suit(), trump))

              // the defending card played must be greater than the attacking card
              val allowed_cards_by_rank = allowed_cards_by_suit.filter(
                _.get_n_rank(trump) > attack_card.get_n_rank(trump)
              )
              // println(s"allowed cards by rank: ${allowed_cards_by_rank.map(_.show_card())}")

              // if the defender has a card that can beat the attacking card, they play the lowest one
              if allowed_cards_by_rank.nonEmpty then
                val card_to_play =
                  allowed_cards_by_rank.minBy(_.get_n_rank(trump))

                play_card(card_to_play)
                played_cards = card_to_play :: played_cards
                defense_counter += 1

            // if the player cannot defend, they take the attacking cards
            if defense_counter < attacking_cards_in_play.length then
              defending_cards_in_play.foreach(card => hand.add_card(card))
              attacking_cards_all.foreach(card => hand.add_card(card))
              return (List(), defending_cards_in_play, false)

            // if the player can defend, they play those cards
            // println(s"played cards: ${played_cards.map(_.show_card())}")
            return (
              attacking_cards_all,
              played_cards ::: defending_cards_in_play,
              true
            )
        else
          // in the trump hoarder strategy, the player only ever plays
          // the lowest-ranked card in their hand when the player is the attacker

          if !first_attack then
            val cards = hand.get_cards()
            var cards_to_play: List[Card] = List()
            var total_cards_in_play =
              attacking_cards_in_play ::: defending_cards_in_play
            for card_in_play <- total_cards_in_play do
              for card <- cards do
                if card.get_suit() == card_in_play.get_suit() then
                  cards_to_play = card :: cards_to_play

            var cards_to_return: List[Card] = List()
            var card_count = 0
            for card <- cards_to_play do
              if Math.min(
                  6,
                  defending_player_hand_size
                ) - card_count - attacking_cards_in_play.length > 0
              then
                play_card(card)
                card_count += 1
                cards_to_return = card :: cards_to_return
            return (
              cards_to_return ::: attacking_cards_in_play,
              defending_cards_in_play,
              true
            )

          // next, get all cards of the lowest rank
          val lowest_rank =
            hand.get_cards().minBy(_.get_n_rank(trump)).get_n_rank(trump)
          val cards_to_play =
            hand.get_cards().filter(_.get_n_rank(trump) == lowest_rank)

          // println(s"lowest rank: $lowest_rank")

          cards_to_play.foreach(play_card)
          return (
            cards_to_play ::: attacking_cards_in_play,
            defending_cards_in_play,
            true
          )
      case "aggressive" =>
        if !is_attacker then
          // in the default strategy, the player only plays the
          // lowest-ranked card in their hand when the player is the defender
          // If they cannot defend, he will take the attacking card
          var played_cards: List[Card] = List()
          // var cannot_defend = true

          // the defender has to defend against each attacking card in play
          // responding with
          var cards_to_play: List[Card] = List()
          var total_defense: Boolean = true
          for attack_card <- attacking_cards_in_play do
            // println(s"attack card: ${attack_card.show_card()}")
            // the defender can respond with any card of the same suit as the attacking card, or trump
            val allowed_cards_by_suit =
              hand.get_cards_by_suits(Set(attack_card.get_suit(), trump))

            // the defending card played must be greater than the attacking card
            val allowed_cards_by_rank = allowed_cards_by_suit.filter(
              _.get_n_rank(trump) > attack_card.get_n_rank(trump)
            )
            // println(s"allowed cards by rank: ${allowed_cards_by_rank.map(_.show_card())}")

            // if the defender has a card that can beat the attacking card, they play the lowest one
            if allowed_cards_by_rank.nonEmpty && 
               allowed_cards_by_rank.filter(x => !cards_to_play.contains(x)).nonEmpty then

              cards_to_play = allowed_cards_by_rank
                .filter(x => !cards_to_play.contains(x))
                .minBy(
                  _.get_n_rank(trump)
                ) :: cards_to_play
            else total_defense = false

          if total_defense then
            for card <- cards_to_play do
              play_card(card)
              played_cards = card :: played_cards

          // if the player cannot defend, they take the attacking cards
          else
            attacking_cards_all.foreach(card => hand.add_card(card))
            defending_cards_in_play.foreach(card => hand.add_card(card))
            return (List(), List(), false)

          // if the player can defend, they play those cards
          return (
            attacking_cards_all,
            played_cards ::: defending_cards_in_play,
            true
          )
        else
          // in the aggressive attacker strategy, the player plays
          // the highest-ranked card in their hand when the player is the attacker

          if !first_attack then
            val cards = hand.get_cards()
            var cards_to_play: List[Card] = List()
            var total_cards_in_play =
              attacking_cards_in_play ::: defending_cards_in_play
            for card_in_play <- total_cards_in_play do
              for card <- cards do
                if !cards.contains(card_in_play) then
                  if card.get_rank() == card_in_play.get_rank() then
                    cards_to_play = card :: cards_to_play

            var cards_to_return: List[Card] = List()
            var card_count = 0
            for card <- cards_to_play do
              if Math.min(6, defending_player_hand_size) - card_count > 0
              then
                if !(attacking_cards_in_play ::: cards_to_return).contains(card)
                then
                  play_card(card)
                  card_count += 1
                  cards_to_return = card :: cards_to_return
            return (
              cards_to_return ::: attacking_cards_in_play,
              defending_cards_in_play,
              true
            )

          // get all cards of the highest rank
          val highest_rank =
            hand.get_cards().maxBy(_.get_n_rank(trump)).get_n_rank(trump)
          val cards_to_play =
            hand.get_cards().filter(_.get_n_rank(trump) == highest_rank)
          // println(s"lowest rank: $lowest_rank")

          cards_to_play.foreach(play_card)
          return (
            cards_to_play ::: attacking_cards_in_play,
            defending_cards_in_play,
            true
          )
      case "chaotic" =>
        if !is_attacker then
          // in the default strategy, the player only plays the
          // lowest-ranked card in their hand when the player is the defender
          // If they cannot defend, he will take the attacking card
          var played_cards: List[Card] = List()
          var cannot_defend = true

          // the defender has to defend against each attacking card in play
          // responding with
          for attack_card <- attacking_cards_in_play do
            // println(s"attack card: ${attack_card.show_card()}")
            // the defender can respond with any card of the same suit as the attacking card, or trump
            val allowed_cards_by_suit =
              hand.get_cards_by_suits(Set(attack_card.get_suit(), trump))

            // the defending card played must be greater than the attacking card
            val allowed_cards_by_rank = allowed_cards_by_suit.filter(
              _.get_n_rank(trump) > attack_card.get_n_rank(trump)
            )
            // println(s"allowed cards by rank: ${allowed_cards_by_rank.map(_.show_card())}")

            // if the defender has a card that can beat the attacking card, they play the lowest one
            if allowed_cards_by_rank.nonEmpty then
              val allowed_cards_len = allowed_cards_by_rank.length
              val rand_idx = Random.nextInt(allowed_cards_len)
              val card_to_play =
                allowed_cards_by_rank(rand_idx)

              play_card(card_to_play)
              played_cards = card_to_play :: played_cards
              cannot_defend = false

          // if the player cannot defend, they take the attacking cards
          if cannot_defend then
            defending_cards_in_play.foreach(card => hand.add_card(card))
            attacking_cards_all.foreach(card => hand.add_card(card))

            return (List(), defending_cards_in_play, false)

          // if the player can defend, they play those cards
          return (
            attacking_cards_all,
            played_cards ::: defending_cards_in_play,
            true
          )
        else
          val cards = hand.get_cards()
          val ranks = cards.map(_.get_n_rank(trump)).distinct
          val randomRank = ranks(Random.nextInt(ranks.length))
          val cards_to_play = cards.filter(_.get_n_rank(trump) == randomRank)

          cards_to_play.foreach(play_card)
          return (
            cards_to_play ::: attacking_cards_in_play,
            defending_cards_in_play,
            true
          )

  }
  def setStrategy(new_strategy: String): String =
    strategy = new_strategy
    showStrategy()

  def showStrategy(): String =
    name + "'s strategy is " + strategy

}
