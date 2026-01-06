package view

import controller._
import model._
import scripts._

import java.awt.{
  AlphaComposite,
  BasicStroke,
  Color,
  Dimension,
  Insets,
  Rectangle,
  Font,
  Graphics2D,
  RenderingHints
}
import java.awt.event.{ActionEvent, ActionListener}
import javax.swing.Timer
import scripts.SoundManager
import scala.swing._
import BorderPanel.Position._
import javax.sound.sampled._
import java.io.File
import scripts.Menu

import scala.collection.mutable.ListBuffer

class SimpleView(window: Frame) extends View {

  override def init(controller: Controller): Unit = {
    this.controller = Some(controller)
    update_PlayerOrder
    update_GameArea
    window.visible = true

    strategyPanel.initializePanels(controller.getPlayers)

    SoundManager.playMusic("resources/sfx/game_theme.wav")

    window.peer.getLayeredPane.add(animationOverlay.peer, new Integer(1))
    animationOverlay.peer.setBounds(0, 0, window.size.width, window.size.height)
  }

  def stopGameOverAnimation(): Unit = {
    animationTimer.foreach(_.stop())
    animationTimer = None
    isGameOver = false
    SoundManager.stopMusic()
    animationOverlay.visible = false
    confettiParticles = ListBuffer.empty[ConfettiParticle]
    winnerName = ""
    window.repaint()
  }

  def initializeGame: Unit = {
    controller.foreach { ctrl =>
      ctrl.initialize(setSeed)

      // Update player strategies based on the selected radio buttons
      strategyPanel.playerPanels.foreach { case (player, _, radioButtons) =>
        radioButtons.zip(strategyPanel.strategies).foreach {
          case (rb, strategy) =>
            if rb.selected then ctrl.setStrategy(strategy, player)
        }
      }
      updateTrumpCardImage(ctrl.getTrumpCard())
      // val remainingCards = controller.get.getUndealtCards()
      // updateUndealtCardsPanel(remainingCards)
    }
   
    animationOverlay.visible = false
    isGameOver = false
    update_GameArea
    SoundManager.playSfx("resources/sfx/riffle.wav")
  }

  def update_PlayerOrder: Unit = {}

  def update_GameArea: Unit = {
    if controller.isDefined then
      val (defender, attackers) = controller.get.getPlayerInfo
      setHands(defender, attackers)
      val (defending_cards, attacking_cards) = controller.get.getCardsInPlay
      playArea.updateCards(defending_cards, attacking_cards)
      val remainingCards = controller.get.getUndealtCards()
      updateUndealtCardsPanel(remainingCards)
  }

  def showWinner(result: String): Unit = {
    // Stop and dispose of the previous timer if it exists
    animationTimer.foreach(_.stop())
    animationTimer = None

    if !isGameOver then SoundManager.playSfx("resources/sfx/laugh.wav")

    winnerName = result
    isGameOver = true

    // Generate confetti particles
    confettiParticles = ListBuffer.fill(300)(
      ConfettiParticle(
        util.Random.nextInt(window.size.width),
        util.Random.nextInt(window.size.height)
      )
    )

    animationOverlay.visible = true

    // Create and start a new timer for the animation
    animationTimer = Some(
      new Timer(
        16,
        new ActionListener {
          def actionPerformed(e: ActionEvent): Unit = {
            animFrame += 1
            confettiParticles =
              confettiParticles.filter(_.gety <= window.size.height + 20)
            confettiParticles.foreach { particle => particle.fall() }
            animationOverlay.repaint(
              new Rectangle(0, 0, window.size.width, window.size.height)
            )
          }
        }
      )
    )
    animationTimer.foreach(_.start())

    window.repaint()
  }

  val playArea = new PlayArea(List(), List())
  val defenderPanel = new HandPanel("Defender", List())
  val attackerPanels = List(
    new HandPanel("Attacker 1", List()),
    new HandPanel("Attacker 2", List()),
    new HandPanel("Attacker 3", List())
  )

  val undealtCardPanel = new HandPanel("Undealt Cards", List())
  val cardBackImage: java.awt.Image = javax.imageio.ImageIO.read(
    new java.io.File("resources/images/cards/back.jpg")
  )
  def getCardBacks(count: Int): List[Card] = {
    List.fill(count)(Card("Back", "Back", 0))
  }

  def updateUndealtCardsPanel(undealtCardsCount: Int): Unit = {
    // val cardBacks = getCardBacks(undealtCardsCount)
    // undealtCardPanel.updateHand(cardBacks)

    undealtCardPanel.updateTitle(s"$undealtCardsCount")
    if controller.get.getUndealtCards() != 0 then {
      undealtCardPanel.updateHand(List(new Card("NULL", "NULL", 0, true)))
      undealtCardPanel.repaint()
    } else {
      undealtCardPanel.updateHand(List())
    }
  }

  def updateTrumpCardImage(card: Card): Unit = {
    trumpCardImage =
      card.rawImage // Assuming `rawImage` is the original image in the `Card` class
    trump_Card_panel.repaint()
  }

  val trump_Card_panel = new HandPanel("Trump Card", List())
  var trumpCardImage: java.awt.Image = null

  def setHands(defender: Player, attackers: List[Player]): Unit = {
    defenderPanel.updateHand(defender.get_hand().get_cards())
    defenderPanel.updateTitle(defender.get_name())
    trump_Card_panel.updateHand(List(controller.get.getTrumpCard()))
    trump_Card_panel.updateTitle(controller.get.getTrumpCard().show_card())
    undealtCardPanel.updateTitle(
      s"${controller.get.getUndealtCards()}"
    )
    // undealtCardPanel.updateHand(deck.convert_draw_pile())

    for i <- 0 to 2 do
      attackerPanels(i).updateHand(attackers(i).get_hand().get_cards())
      attackerPanels(i).updateTitle(attackers(i).get_name())
  }

  object GamePanel extends BorderPanel {
    preferredSize = new Dimension(800, 500)

    layout(new FlowPanel {
      background = Color.BLUE
      preferredSize = new Dimension(800, 40)

      private val backgroundImage = javax.imageio.ImageIO.read(
        new java.io.File("resources/images/scroll.png")
      )

      override def paintComponent(g: Graphics2D): Unit = {
        super.paintComponent(g)
        if (backgroundImage != null) {
          g.drawImage(backgroundImage, 0, 0, size.width, size.height, null)
        }
      }

      private val randomnessToggle = new CheckBox("Set Seed") {
        selected = true // Default to randomness enabled
        reactions += { case event.ButtonClicked(_) =>
          setSeed = this.selected
        }
      }

      contents ++= Seq(
        createButton("Initialize Game", () => initializeGame, width = 120),
        createButton(
          "Do Move",
          () =>
            if !isGameOver then {
              controller.foreach(_.doMove())
              SoundManager.playSfx("resources/sfx/card_1.wav")
            }
        ),
        createButton(
          "Do Turn",
          () =>
            if !isGameOver then {
              controller.foreach(_.doTurn())
              SoundManager.playSfx("resources/sfx/riffle.wav")
            }
        ),
        createButton(
          "Do Game",
          () => if !isGameOver then controller.foreach(_.doGame())
        ),
        createButton(
          ">> Advance",
          () => controller.foreach(_.advanceOrder()),
          width = 120
        ),
        randomnessToggle // Add the toggle to the UI

      )
    }) = North

    layout(new AbsolutePositionPanel {
      background = new Color(200, 80, 80)
      preferredSize = new Dimension(1200, 430)
      val panelWidth = preferredSize.width
      val panelHeight = preferredSize.height

      addComponent(playArea, 112, 175, 620, 125)
      addComponent(attackerPanels(0), 28, 25, 250, 125)
      addComponent(attackerPanels(1), 304, 25, 250, 125)
      addComponent(attackerPanels(2), 581, 25, 250, 125)
      addComponent(defenderPanel, 302, 325, 250, 125)
      addComponent(trump_Card_panel, 612, 325, 175, 125)
      addComponent(
        undealtCardPanel,
        65,
        325,
        175,
        125
      ) // Adjust position and size as needed

    }) = Center
  }

  class AbsolutePositionPanel extends Panel {
    peer.setLayout(null)

    private val backgroundImage =
      javax.imageio.ImageIO.read(new java.io.File("resources/images/table.jpg"))

    override def paintComponent(g: Graphics2D): Unit = {
      super.paintComponent(g)
      if (backgroundImage != null) {
        g.drawImage(backgroundImage, 0, 0, size.width, size.height, null)
      }
    }

    def addComponent(
        comp: Component,
        x: Int,
        y: Int,
        width: Int,
        height: Int
    ): Unit = {
      peer.add(comp.peer)
      comp.peer.setBounds(x, y, width, height)
    }
  }

  def createButton(
      name: String,
      on_press: () => Unit,
      width: Int = 80
  ): Button = new Button(name) {
    preferredSize = new Dimension(width, 30)
    margin = new Insets(2, 5, 2, 5)
    focusPainted = false

    reactions += { case event.ButtonClicked(_) =>
      on_press()
    }
  }

  object strategyPanel extends BorderPanel {
    val strategies = List("default", "trump hoarder", "aggressive", "chaotic")
    val titleFont = new Font("Arial", Font.BOLD, 14)
    var playerPanels: List[(Player, ButtonGroup, List[RadioButton])] = List()

    def initializePanels(players: List[Player]): Unit = {
      playerPanels = players.map { player =>
        val buttonGroup = new ButtonGroup
        val radioButtons = strategies.map { strategy =>
          val rb = new RadioButton("")
          rb.tooltip = strategy
          rb.horizontalAlignment = Alignment.Center
          rb.reactions += { case event.ButtonClicked(_) =>
            updatePlayerStrategy(player, strategy)
            println(player.showStrategy())
          }
          buttonGroup.buttons += rb
          rb
        }
        radioButtons.head.selected = true
        (player, buttonGroup, radioButtons)
      }
      updateLayout()
    }

    def updatePlayerStrategy(player: Player, strategy: String): Unit = {
      if controller.isDefined then controller.get.setStrategy(strategy, player)
    }

    layout(new FlowPanel {
      background = new Color(220, 220, 220)
      contents += new Label("Strategy Selection") {
        font = new Font("Arial", Font.BOLD, 16)
      }
    }) = North

    def updateLayout(): Unit = {
      val rows = playerPanels.length + 1
      val cols = strategies.length + 1

      layout(new GridPanel(rows, cols) {
        background = new Color(240, 240, 240)
        border = Swing.EmptyBorder(10, 10, 10, 10)

        def cellWrapper(component: Component): Component = new BorderPanel {
          border = javax.swing.BorderFactory.createLineBorder(Color.GRAY, 1)
          layout(component) = Center
        }

        contents += cellWrapper(new Label("") {
          font = titleFont; horizontalAlignment = Alignment.Center
        })
        strategies.foreach { strategy =>
          contents += cellWrapper(new Label(strategy) {
            font = titleFont; horizontalAlignment = Alignment.Center
          })
        }

        playerPanels.foreach { (player, _, radioButtons) =>
          contents += cellWrapper(new Label(player.get_name()) {
            font = titleFont; horizontalAlignment = Alignment.Left
          })
          radioButtons.foreach { rb =>
            contents += cellWrapper(rb)
          }
        }
      }) = Center
    }
  }

  private var isGameOver = false
  private var winnerName = ""
  private var confettiParticles = ListBuffer.empty[ConfettiParticle]
  private var setSeed = true

  private var animFrame = 0
  private var animationTimer: Option[Timer] = None

  object animationOverlay extends Panel {
    opaque = false
    visible = false

    override def paintComponent(g: Graphics2D): Unit = {
      if (!isGameOver) return

      val g2d = g.asInstanceOf[Graphics2D]
      g2d.setRenderingHint(
        RenderingHints.KEY_ANTIALIASING,
        RenderingHints.VALUE_ANTIALIAS_ON
      )

      confettiParticles.foreach { confetti =>
        g2d.setColor(confetti.color)
        g2d.fillOval(confetti.getx, confetti.gety, 5, 5)
      }

      val bannerWidth = 400
      val bannerHeight = 100
      val centerX = size.width / 2
      val centerY = size.height / 2
      val x = centerX - bannerWidth / 2
      val y = centerY - bannerHeight / 2

      g2d.setColor(new Color(255, 255, 255, 220))
      g2d.fillRoundRect(x, y, bannerWidth, bannerHeight, 30, 30)

      g2d.setColor(Color.BLACK)
      g2d.setFont(new Font("Arial", Font.BOLD, 32))

      val message = s"${winnerName} Wins!"
      val metrics = g2d.getFontMetrics
      val msgWidth = metrics.stringWidth(message)

      g2d.drawString(
        message,
        centerX - msgWidth / 2,
        centerY + metrics.getAscent / 2 - 5
      )
    }
  }

  case class ConfettiParticle(_x: Int, _y: Int, color: Color = randomColor()) {
    private var x = _x
    private var y = _y

    def gety = y
    def getx = x

    def fall(): Unit = {
      x = x + util.Random.nextInt(3) - 1
      y = y + 5 * util.Random.nextInt(4) + 1
    }
  }

  def randomColor(): Color = {
    val colors = Array(
      new Color(255, 50, 50),
      new Color(255, 150, 0),
      new Color(255, 255, 0),
      new Color(100, 255, 100),
      new Color(0, 200, 255),
      new Color(150, 100, 255),
      new Color(255, 100, 255),
      new Color(255, 215, 0)
    )
    colors(util.Random.nextInt(colors.length))
  }

  object borderPanel extends BorderPanel {
    layout(GamePanel) = Center
    layout(strategyPanel) = South
  }

  // window.title = "Durak"
  window.contents = borderPanel
  window.centerOnScreen()
  window.size = new Dimension(875, 720)

  window.peer.setDefaultCloseOperation(
    javax.swing.WindowConstants.DO_NOTHING_ON_CLOSE
  )
  window.peer.addWindowListener(new java.awt.event.WindowAdapter {
    override def windowClosing(e: java.awt.event.WindowEvent): Unit = {
      SoundManager.stopMusic()
      window.visible = false
      window.dispose()
    }
  })
}
