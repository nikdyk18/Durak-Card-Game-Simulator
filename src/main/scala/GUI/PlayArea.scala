package view

import scripts._
import scala.swing._
import java.awt.{Color, Dimension, Font, BasicStroke, Graphics2D, RenderingHints}
import javax.swing.ImageIcon
import scripts.SoundManager

/**
 * A panel that displays a player's hand of cards
 * Can be freely positioned within the GamePanel
 */

class PlayArea(dCards: List[Card], aCards: List[Card]) extends GridPanel(1, 2) {
  // Create hand panels for attacking and defending cards
  private val attackingPanel = new HandPanel("Attacking Cards", aCards, 500)
  private val defendingPanel = new HandPanel("Defending Cards", dCards, 500)

  // Add panels to the grid
  contents += attackingPanel // Left side
  contents += defendingPanel // Right side

  // Method to update the cards in the hand panels
  def updateCards(dCards: List[Card], aCards: List[Card]): Unit = {
    attackingPanel.updateHand(aCards)
    defendingPanel.updateHand(dCards)
    repaint()
  }
}