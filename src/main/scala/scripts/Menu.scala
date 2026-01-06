package scripts

/** Represents the menu interface of the game.
  */
class Menu {
  private val game = new Game

  /** Initializes the game.
    */
  def initializeGame(setSeed: Boolean = true): Unit =
    game.initializeGame(setSeed)

  /** Checks for a winner
    */
  def checkForWinner: Option[String] =
    game.checkForWinner

  /** Displays the player order.
    *
    * @return
    *   A string representation of the player order.
    */
  def showPlayerOrder: String =
    game.player_order.showPlayerOrder

  def getPlayerOrder: PlayerOrder =
    game.player_order

  def get_trump_card: Card =
    game.get_trump_card()

  def getNumberUndealtCards: Int =
    game.getNumberUndealtCards()

  /** Advances the player order.
    *
    * @return
    *   A string representation of the new player order.
    */
  def advancePlayerOrder: String =
    game.advancePlayerOrder

  /** Displays the game area.
    *
    * @return
    *   A string representation of the game area.
    */
  def showGameArea: String =
    game.showGameArea

  /** Performs a single move.
    */
  def doMove: String =
    game.doMove

  /** Performs a single turn.
    */
  def doTurn: String =
    for (a <- 0 until 4) if game.checkForWinner == None then doMove
    showGameArea

  /** Performs doTurn until there is a winner.
    */
  def doGame: String =
    while (game.checkForWinner == None) do
      // doMove not doTurn, because someone could win during a turn
      doMove
    showGameArea

  def setStrategy(strategy: String, player: Player): String =
    game.setStrategy(strategy, player)

  def showStrategies(): String =
    game.showStrategies()

  def getPlayerInfo(): (Player, List[Player]) =
    game.getPlayerInfo

  def getPlayers(): List[Player] =
    game.getPlayers

  def getCardsInPlay(): (List[Card], List[Card]) =
    game.getCardsInPlay

  /** Empties a player's hand, for testing purposes.
    */
  def emptyHand: Unit =
    game.discardFirstHand
}
