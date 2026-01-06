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

 class HandPanel(title: String, initialCards: List[Card], width: Int = 400) extends Panel {
  // Card display properties
  private var cardOverlap = 10 // Default overlap between cards

  // Colors
  private val backgroundColor = new Color(220, 220, 220)
  private val titleColor = new Color(60, 60, 60)
  private val selectedBorderColor = new Color(255, 215, 0) // Gold highlight

  // The current hand of cards to display
  private var cards = initialCards

  // The title of the panel
  private var displayTitle = ""

  // Set the preferred size based on expected max cards
  preferredSize = new Dimension(width, 150)

  // Method to update the cards in the hand
  def updateHand(newCards: List[Card]): Unit = {
    cards = newCards
    adjustCardOverlap() // Adjust overlap to ensure all cards are visible
    repaint()
  }

  def updateTitle(newTitle: String): Unit =
    displayTitle = newTitle

  // Adjust the card overlap dynamically to ensure all cards fit within the panel
  private def adjustCardOverlap(): Unit = {
    if (cards.nonEmpty) {
      val cardWidth = cards.head.image.getWidth // Assume all cards have the same width
      val totalCardWidth = cards.length * cardWidth
      val availableWidth = size.width - 40 // Leave some padding on both sides

      // If cards exceed the available width, adjust the overlap
      if (totalCardWidth > availableWidth) {
        cardOverlap = Math.max(1, cardWidth - (availableWidth / cards.length)+5)
      } else {
        cardOverlap = 30 // Reset to default overlap if there's enough space
      }
    }
  }

  override def paintComponent(g: Graphics2D): Unit = {
    // Enable anti-aliasing for smoother drawing
    g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON)

    // Draw panel background
    g.setColor(backgroundColor)
    g.fillRoundRect(0, 0, size.width, size.height, 10, 10)

    // Draw panel border
    g.setColor(new Color(180, 180, 180))
    g.setStroke(new BasicStroke(2))
    g.drawRoundRect(1, 1, size.width - 2, size.height - 2, 10, 10)

    // Draw title
    g.setColor(titleColor)
    g.setFont(new Font("Dialog", Font.BOLD, 14))
    g.drawString(f"$title: $displayTitle", 10, 20)

    // Draw cards - we want cards on the right to overlap cards on the left
    // so we draw them in reverse order to ensure proper z-ordering
    for (i <- cards.indices.reverse) {
      // Calculate the position of this card
      val card = cards(i)
      val cardWidth = card.image.getWidth
      val cardHeight = card.image.getHeight

      val xPos = 20 + (i * (cardWidth - cardOverlap))
      val yPos = 25

      // Draw the card image
      if (card.image != null) {
        g.drawImage(card.image, xPos, yPos, cardWidth, cardHeight, null)
      } else {
        // Fallback if image is null - draw a simple placeholder
        g.setColor(Color.WHITE)
        g.fillRoundRect(xPos, yPos, cardWidth, cardHeight, 8, 8)
        g.setColor(Color.BLACK)
        g.drawRoundRect(xPos, yPos, cardWidth, cardHeight, 8, 8)
        g.drawString("Card", xPos + 10, yPos + cardHeight / 2)
      }
    }
  }
}