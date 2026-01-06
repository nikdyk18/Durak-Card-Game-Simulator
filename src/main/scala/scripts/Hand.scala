package scripts

/** Represents a hand of cards in the game.
  */
class Hand {
  private var cards = List.empty[Card]

  /** Checks if the hand is empty.
    *
    * @return
    *   True if the hand is empty, otherwise false.
    */
  def isEmpty(): Boolean =
    cards.isEmpty

  /** Discards the entire hand.
    */
  def discardHand(): Unit =
    cards = List.empty[Card]

  def get_cards(): List[Card] =
    cards

  /** Displays all cards in the hand.
    *
    * @return
    *   A string representation of the cards in the hand.
    */
  def show_cards(): String =
    cards
      .foldRight("")((item, acc) => acc + item.show_card() + ", ")
      .dropRight(2) // get rid of the trailing comma

  /** Adds a card to the hand.
    *
    * @param card
    *   The card to add.
    */
  def add_card(card: Card): Unit =
    if !cards.contains(card) then cards = card :: cards

    /** Discards a card from the hand
      */
  def discard_card(card: Card): Unit =
    cards = cards.filterNot(_ == card)

  /** Checks if the hand is valid.
    *
    * @return
    *   True if the hand is valid, otherwise false.
    */
  def is_valid_hand(): Boolean =
    val not_all_same_color = Set(cards.foreach(_.get_color())).size == 2
    val not_five_of_same_suit = List(cards.foreach(_.get_suit()))
      .groupBy(identity)
      .mapValues(_.length)
      .values
      .max < 5
    not_all_same_color && not_five_of_same_suit

  /** Retrieves the lowest trump card in the hand.
    *
    * @param trump
    *   The trump suit.
    * @return
    *   An optional card, if a valid trump card exists.
    */
  def get_lowest_trump(trump: String): Option[Card] =
    cards
      .filter(_.is_trump(trump))
      .foldRight(None)((item, acc) =>
        if acc == None then Some(item)
        else if item.is_lower_than(acc.get, trump) then Some(item)
        else acc
      )

  /** Retrieves the lowest card in the hand.
    * @return
    *   The lowest card in the hand.
    */
  def get_lowest_card(trump: String): Card =
    // gets the lowest card in the hand,
    // with consideration of trump suit
    cards.minBy(_.get_n_rank(trump))

  /** Retrieves the highest card in the hand.
    * @return
    *   The highest card in the hand.
    */
  def get_highest_card(trump: String): Card =
    // gets the highest card in the hand,
    // with consideration of trump suit
    cards.maxBy(_.get_n_rank(trump))

  /** Get cards by suit
    * @param suit
    *   The suit to filter by
    * @return
    *   A list of cards of the specified suit
    */
  def get_cards_by_suits(suits: Set[String]): List[Card] =
    cards.filter(card => suits.contains(card.get_suit()))

  /** Get cards by rank
    * @param rank
    *   The rank to filter by
    * @return
    *   A list of cards of the specified rank
    */
  def get_cards_by_ranks(ranks: Set[String]): List[Card] =
    cards.filter(card => ranks.contains(card.get_rank()))
}
