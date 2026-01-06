package scripts

/** Represents a card in the game.
  *
  * @param rank
  *   The rank of the card.
  * @param suit
  *   The suit of the card.
  * @param n_rank
  *   The numerical rank of the card used for comparison.
  */
class Card(
    rank: String,
    suit: String,
    n_rank: Int,
    just_back: Boolean = false
) {

  // Read the original image
  var rawImage: java.awt.image.BufferedImage = null
  if just_back then
    rawImage = javax.imageio.ImageIO.read(
      new java.io.File("resources/images/cards/back.jpg")
    )
  // Read the image from the file system
  else
    rawImage = javax.imageio.ImageIO.read(
      new java.io.File(
        f"resources/images/cards/${n_rank + 6}${suit.toLowerCase().head}.jpg"
      )
    )

  // Set scaling parameters
  val scaleFactor = 0.8
  val originalWidth = rawImage.getWidth
  val originalHeight = rawImage.getHeight
  val newWidth = (originalWidth * scaleFactor).toInt
  val newHeight = (originalHeight * scaleFactor).toInt

  // Scale the image
  val scaledImage =
    rawImage.getScaledInstance(newWidth, newHeight, java.awt.Image.SCALE_SMOOTH)
  val image = new java.awt.image.BufferedImage(
    newWidth,
    newHeight,
    java.awt.image.BufferedImage.TYPE_INT_ARGB
  )
  val g2d = image.createGraphics()
  g2d.drawImage(scaledImage, 0, 0, null)
  g2d.dispose()

  /** Displays the card.
    *
    * @return
    *   A string representation of the card.
    */
  def show_card(): String =
    rank + suit

  /** Retrieves the rank of the card.
    *
    * @return
    *   The rank of the card.
    */
  def get_rank(): String =
    rank

  /** Retrieves the suit of the card.
    *
    * @return
    *   The suit of the card.
    */
  def get_suit(): String =
    suit

  /** Retrieves the numerical rank of the card.
    *
    * @return
    *   The numerical rank of the card.
    */
  def get_n_rank(trump: String): Int =
    if is_trump(trump) then n_rank + 100
    else n_rank

  /** Retrieves the color of the card.
    *
    * @return
    *   The color of the card.
    */
  def get_color(): String =
    if List("Heart", "Diamond").contains(suit) then "red"
    else "black"

  /** Checks if the card is a trump card.
    *
    * @param trump
    *   The trump suit.
    * @return
    *   True if the card is a trump card, otherwise false.
    */
  def is_trump(trump: String): Boolean =
    suit == trump

  /** Compares the current card with another card to see if it is higher.
    *
    * @param card
    *   The card to compare with.
    * @param trump
    *   The trump suit.
    * @return
    *   True if the current card is higher, otherwise false.
    */
  def is_greater_than_or_equal_to(card: Card, trump: String): Boolean =
    get_n_rank(trump) > card.get_n_rank(trump)

  /** Compares the current card with another card to see if it is lower.
    *
    * @param card
    *   The card to compare with.
    * @param trump
    *   The trump suit.
    * @return
    *   True if the current card is lower, otherwise false.
    */
  def is_lower_than(card: Card, trump: String): Boolean =
    get_n_rank(trump) < card.get_n_rank(trump)
}
