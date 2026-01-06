package view

import scripts.SoundManager
import view.SimpleView

import scala.swing._
import java.awt.{Color, Graphics2D, Image}
import javax.imageio.ImageIO
import java.io.File
import scala.swing.event.ButtonClicked
import controller._
import model._
import login._

class TitleView(model: Model, window: Frame) extends View {

  // Try to start music but don't fail if it doesn't work
  try {
    SoundManager.playMusic("resources/sfx/title_theme.wav")
  } catch {
    case e: Exception => println(s"Could not play title music: ${e.getMessage}")
  }

  // Load background image with proper error handling
  private val backgroundImage: Image =
    try {
      val file = new File("resources/images/title.png")
      if (!file.exists()) {
        println(s"Background image file not found at ${file.getAbsolutePath}")
        null
      } else {
        ImageIO.read(file)
      }
    } catch {
      case e: Exception =>
        println(s"Error loading background image: ${e.getMessage}")
        null
    }

  // PLAY button with improved styling
  val playButton = new Button("PLAY") {
    preferredSize = new Dimension(180, 60)
    font = new Font("Arial", java.awt.Font.BOLD, 28)
    background = new Color(220, 180, 80)
    foreground = Color.WHITE
    focusPainted = false // removes focus border

  }

  val loginButton = new Button("LOGIN") {
    preferredSize = new Dimension(180, 60)
    font = new Font("Arial", java.awt.Font.BOLD, 20)
    background = new Color(220, 180, 80)
    foreground = Color.WHITE
    focusPainted = false // removes focus border

  }

  val registerButton = new Button("REGISTER") {
    preferredSize = new Dimension(180, 60)
    font = new Font("Arial", java.awt.Font.BOLD, 20)
    background = new Color(220, 180, 80)
    foreground = Color.WHITE
    focusPainted = false // removes focus border

  }

  // Background panel with fallback
  object imagePanel extends Panel {
    override def paintComponent(g: Graphics2D): Unit = {
      super.paintComponent(g)
      if (backgroundImage != null) {
        g.drawImage(backgroundImage, 0, 0, size.width, size.height, null)
      } else {
        // Fallback drawing if image fails to load
        g.setColor(new Color(30, 60, 90))
        g.fillRect(0, 0, size.width, size.height)
        g.setColor(Color.WHITE)
        val titleFont = new Font("Arial", java.awt.Font.BOLD, 48)
        g.setFont(titleFont)
        val fm = g.getFontMetrics(titleFont)
        val textWidth = fm.stringWidth("DURAK")
        g.drawString("DURAK", size.width / 2 - textWidth / 2, size.height / 3)
      }
    }
    preferredSize = new Dimension(800, 600)
    peer.setLayout(null) // allow absolute positioning
    peer.add(playButton.peer) // add the button
    playButton.peer.setBounds(310, 360, 180, 60) // set bounds after adding
    peer.add(loginButton.peer) // add the button
    loginButton.peer.setBounds(20, 20, 140, 40) // set bounds after adding
    peer.add(registerButton.peer) // add the button
    registerButton.peer.setBounds(20, 70, 140, 40) // set bounds after adding

  }

  // Layout with better positioning
  window.contents = new BorderPanel {
    layout(imagePanel) = BorderPanel.Position.Center
  }

  window.title = "Durak Card Game"
  window.resizable = false
  window.pack()
  window.centerOnScreen()
  window.visible = true

  // Hook up button with safe sound playing
  playButton.reactions += { case ButtonClicked(`playButton`) =>
    SoundManager.stopMusic()
    SoundManager.playSfx("resources/sfx/game_start.wav", 5.0)
    model.menu.initializeGame()
    controller.get.changeView(
      new SimpleView(window)
    ) // avoid opening a new window
  }
  window.listenTo(playButton)

  window.listenTo(loginButton)
  loginButton.reactions += { case ButtonClicked(`loginButton`) =>
    val username =
      Dialog.showInput(window, "Enter your username:", initial = "")
    val password = Dialog.showInput(
      window,
      "Enter your password:",
      initial = "",
      title = "Password",
      messageType = Dialog.Message.Plain,
      icon = null,
      entries = Nil
      // editable = true
    )

    if username.isDefined && password.isDefined then
      if AuthSystem.authenticate(username.get, password.get) then
        window.title = s"Durak Card Game - Welcome, ${username.get}!"
        Dialog.showMessage(
          window,
          s"Welcome, ${username.get}!",
          title = "Login Successful"
        )
      else
        Dialog.showMessage(
          window,
          "Invalid username or password.",
          title = "Login Failed",
          messageType = Dialog.Message.Error
        )
  }

  registerButton.listenTo(registerButton)
  registerButton.reactions += { case ButtonClicked(`registerButton`) =>
    val username = Dialog.showInput(window, "Enter a username:", initial = "")
    val password = Dialog.showInput(
      window,
      "Enter a password:",
      initial = "",
      title = "Password",
      messageType = Dialog.Message.Plain,
      icon = null,
      entries = Nil
      // editable = true
    )

    if username.isDefined && password.isDefined then
      if AuthSystem.register(username.get, password.get) then
        Dialog.showMessage(
          window,
          s"Account created successfully for ${username.get}!",
          title = "Registration Successful"
        )
      else
        Dialog.showMessage(
          window,
          "Username already exists. Please choose a different username.",
          title = "Registration Failed",
          messageType = Dialog.Message.Error
        )
  }

  // MVC-required methods
  override def update_PlayerOrder: Unit = {}
  override def update_GameArea: Unit = {}
  override def showWinner(result: String): Unit = {}
}
