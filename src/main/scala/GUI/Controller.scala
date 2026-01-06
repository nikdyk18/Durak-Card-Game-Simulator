package controller

import model._
import scala.swing._
import view._
import scripts._

/** Controller for an MVC architecture
  * @param model
  *   Model for the MVC architecture
  * @param view
  *   View for MVC architecture
  */
class Controller(model: Model, start_view: View) {
  var view = start_view

  def changeView(new_view: View): Unit = {
    view = new_view
    view.init(this)
  }

  /** Displays everything necessary to observe and understand the progress of
    * the game
    */
  def showGameArea: String = {
    model.menu.showGameArea
  }

  /** shows player names in the order they will play
    */
  def showPlayerOrder: String = {
    model.menu.showPlayerOrder
  }

  /** moves the player who is currently "up" to the end of the playing order,
    * and advances the "next" player to be "up"
    */
  def advanceOrder = Action("Advance") {
    model.menu.advancePlayerOrder
    view.update_GameArea
  }

  /** performs all actions necessary to prepare the game for the first move; can
    * also be used to reset the game simulation
    */
  def initialize(setSeed: Boolean = true): Unit = {
    model.menu.initializeGame(setSeed)
    view.update_GameArea
  }

  /** at any point in the game simulation, determine whether any player has won
    * the game, and return the winning player's name or "none"
    */
  def checkForWinner = Action("Winner?") {
    view.showWinner(model.menu.checkForWinner.getOrElse("None"))
  }

  /** the player who is currently "up" in the player order performs all move
    * actions, and the player order advances
    */
  def doMove = Action("Do Move") {
    // val result = model.menu.doMove
    if model.menu.checkForWinner.isEmpty then model.menu.doMove
    else view.showWinner(model.menu.checkForWinner.getOrElse("None"))
    view.update_PlayerOrder
    view.update_GameArea
   
  }

  /** performs DO MOVE four times (or more specfically, the length of the player
    * order), as well as CHECK FOR WINNER at appropriate times
    */
  def doTurn = Action("Do Turn") {
    // val result = model.menu.doTurn
    if model.menu.checkForWinner.isEmpty then model.menu.doTurn
    else view.showWinner(model.menu.checkForWinner.getOrElse("None"))
    view.update_PlayerOrder
    view.update_GameArea
    // view.showWinner(model.menu.checkForWinner)
  }

  /** performs DO TURN until the game is won
    */
  def doGame = Action("Do Game") {
    if model.menu.checkForWinner.isEmpty then model.menu.doGame

    view.update_PlayerOrder
    view.update_GameArea
    view.showWinner(model.menu.checkForWinner.getOrElse("None"))
  }

  // Additional, view-specific functionality
  def getPlayerInfo = model.menu.getPlayerInfo()

  def getPlayers = model.menu.getPlayers()

  def setStrategy(strategy: String, player: Player): Unit =
    model.menu.setStrategy(strategy, player)

  def getCardsInPlay = model.menu.getCardsInPlay()

  def getTrumpCard(): Card = {
    model.menu.get_trump_card
  }

  def getUndealtCards(): Int = {
    model.menu.getNumberUndealtCards
  }

  /** terminates the application
    */
  def exit = Action("Exit") {
    sys.exit(0)
  }

}
