package view

import scala.swing._
import controller._
import java.awt.Color
import javax.swing.Timer
import java.awt.event.ActionListener

class SampleAnimationView extends MainFrame with View {
  title = "SampleAnimationGUI"
  contents = animationPanel
  var circleX = 50
  var circleY = 50

  size = new Dimension(600, 700)

  /** Prepare this View class for initial use by invoking the superclass init to
    * store the reference to the controller, hook-up triggers to controller
    * methods, and set visible.
    * @param controller
    *   The MVC Controller
    */
  override def init(controller: Controller): Unit = {
    super.init(controller)

    visible = true
  }

  object animationPanel extends Panel {
    override def paint(g: Graphics2D): Unit =
      g.setColor(Color.red)
      g.fillOval(circleX, circleY, 100, 100)
// THIS IS WHERE DRAWING LOGIC GOES
    val timer = new Timer(
      16,
      new ActionListener {
        override def actionPerformed(e: java.awt.event.ActionEvent): Unit = {
          circleX += 1
          repaint()
        }
      }
    )

    timer.start
  }

  def update_PlayerOrder: Unit =
    ???

  def update_GameArea: Unit =
    ???

  def showWinner(result: String): Unit =
    ???
}
