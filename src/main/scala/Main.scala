import scala.collection.mutable.Queue
import scripts.Menu
import javax.management.modelmbean.ModelMBean
import view.SimpleView
import controller._
import model._

import scala.util.Random
import scala.swing._
import view.{TitleView, SimpleView}

@main def hello(): Unit =
  print("Welcome to Durak!\n")

  val window = new Frame {
    title = "Durak"
    preferredSize = new Dimension(800, 600)
    visible = true
  }

  val my_menu = new Menu
  val model = new Model
  val view = new TitleView(model, window) // start on title screen
  val controller = new Controller(model, view)

  my_menu.initializeGame(true)

  view.init(controller)
