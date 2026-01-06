package scripts

import scala.collection.mutable.Stack
import scala.util.Random

/** Represents a deck of cards in the game.
  */
class Deck {
  private val ranks = List("6", "7", "8", "9", "10", "J", "Q", "K", "A")
  private val suits = List("Clover", "Spade", "Heart", "Diamond")
  private var draw_pile = Stack.empty[Card]
  private var discard_pile = List.empty[Card]

  // Random.setSeed(42) // debugging purposes only
  initializeDeck()

  def initializeDeck(): Unit =

    draw_pile = Stack.empty[Card]
    discard_pile = List.empty[Card]
    // Populate draw pile by iterating over valid suits and ranks

    for suit <- suits do
      for (rank, i) <- ranks.zipWithIndex do draw_pile.push(Card(rank, suit, i))
    shuffle_deck()

  /** Draws a card from the deck.
    *
    * @return
    *   The drawn card.
    */
  def draw_card(): Card =
    if draw_pile.isEmpty then
      draw_pile.pushAll(Random.shuffle(discard_pile))
      discard_pile = List.empty[Card]
    draw_pile.pop()

  /** Adds a card to the discard pile.
    *
    * @param card
    *   The card to add to the discard pile.
    */
  def add_to_discard_pile(card: Card): Unit =
    discard_pile = card :: discard_pile

  /** Retrieves the number of undealt cards in the deck.
    *
    * @return
    *   The number of undealt cards.
    */
  def get_number_undealt_cards(): Int =
    draw_pile.length

  def convert_draw_pile(): List[Card] = {
    draw_pile.map(card => Card("0", "0", 0)).toList
  }

  /** Reveals the last card in the draw pile.
    *
    * @return
    *   The last card in the draw pile.
    */
  def reveal_last_card(): Card =
    if draw_pile.isEmpty then
      new Exception("draw pile is empty upon revealing last card")
    draw_pile.last

  /** Shuffles the deck.
    */
  def shuffle_deck(): Unit =
    val temp = Random.shuffle(draw_pile.toList)
    draw_pile.clear()
    draw_pile.pushAll(temp)
}
