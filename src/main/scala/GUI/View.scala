package view

import controller._

/** View for an MVC architecture Any concrete View class must implement
  * operations to update_PlayerOrder, update_GameArea, and showWinner
  */
trait View {

  var controller: Option[Controller] = None

  /** Initializes the View
    * @param _controller
    *   The Controller for the MVC architecture
    */
  def init(_controller: Controller): Unit = {
    controller = Some(_controller)
  }

  def update_PlayerOrder: Unit

  def update_GameArea: Unit

  def showWinner(result: String ): Unit

}
