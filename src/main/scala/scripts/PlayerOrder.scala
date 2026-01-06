package scripts

import scala.collection.mutable.Queue

/** Manages the order of players in the game.
  */
class PlayerOrder {
  private val player_names = List("Matthew", "Mark", "Luke", "John")
  private val players = player_names.map(name => Player(name))
  private var player_lineup = players.to(Queue)

  private var attacking_player: Option[Player] = None
  private var defending_player: Option[Player] = None

  def reset(): Unit =
    player_lineup = players.to(Queue)
    this.forAllPlayers(player => player.discardHand())
    this.forAllPlayers(player => player.set_defender(false))

  /** Retrieves the lineup of players.
    *
    * @return
    *   A queue of players.
    */
  def get_player_lineup(): Queue[Player] =
    player_lineup

  /** Retrieves the original lineup of players.
    *
    * @return
    *   A queue of players.
    */
  def get_original_player_lineup(): List[Player] =
    players

  /** Retrieves the attacking player based on the trump suit.
    *
    * @param trump
    *   The trump suit.
    * @return
    *   An optional player who is the attacker.
    */
  def get_attacking_player(trump: String): Option[Player] =
    attacking_player = Some(
      player_lineup
        .foldRight(
          (player_lineup.head, player_lineup.head.get_lowest_trump(trump))
        )((player, acc) =>
          val lowest_trump = player.get_lowest_trump(trump)

          (lowest_trump, acc._2) match {
            case (Some(x), Some(y)) =>
              if x.is_lower_than(y, trump) then (player, Some(x))
              else acc
            case (Some(x), None) => (player, Some(x))
            case _ =>
              acc
          }
        )
        ._1
    )
    attacking_player

  /** Retrieves the defending player.
    *
    * @return
    *   An optional player who is the defender.
    */
  def get_defending_player: Option[Player] =
    defending_player = None
    var prev = player_lineup.last
    for player <- player_lineup do
      if prev == attacking_player.get then defending_player = Some(player)
      prev = player

    defending_player

  /** Displays the player order.
    *
    * @return
    *   A string representation of the player order.
    */
  def showPlayerOrder: String =
    var lineup = ""
    player_lineup.foreach(player =>
      if lineup.isEmpty() then lineup += player.get_name()
      else lineup += ", " + player.get_name()
    )
    lineup

  /** Advances the player order.
    *
    * @return
    *   A string representation of the new player order.
    */
  def advance_player_order_n(n: Int = 1): String =
    for i <- 0 until n do player_lineup.enqueue(player_lineup.dequeue())
    showPlayerOrder

  /** Performs a function for all players.
    *
    * @params
    *   f The function to perform.
    */
  def forAllPlayers(f: Player => Unit): Unit =
    player_lineup.foreach(f)

  /** Gets the head of the player lineup.
    *
    * @params
    *   f The function to perform.
    */
  def getHead: Player =
    player_lineup.head
}
