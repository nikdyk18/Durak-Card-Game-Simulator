package scripts

import org.scalatest.funspec.AnyFunSpec
import org.scalatest.matchers.should._
import scripts.Menu

import scala.util.Random

class Menu_Test extends AnyFunSpec with Matchers {
  def beforeEach(): Menu = {
    Random.setSeed(42) // Reset randomness
    val menu = new Menu
    menu.initializeGame()
    menu
  }
  describe("The Durak Simulation") {
    describe("has a menu") {

      // ******* SHOW PLAYER ORDER *******
      it("can show the player order") {
        val expectedResult = "Matthew, Mark, Luke, John"

        val menu = new Menu
        menu.showPlayerOrder should be(expectedResult)
      }

      // ******* ADVANCE ORDER *******
      it("can advance the player order") {
        val expectedResult1 = "Mark, Luke, John, Matthew"
        val expectedResult2 = "Luke, John, Matthew, Mark"
        val expectedResult3 = "John, Matthew, Mark, Luke"
        val expectedResult4 = "Matthew, Mark, Luke, John"

        val menu = new Menu

        menu.advancePlayerOrder should be(expectedResult1)
        menu.advancePlayerOrder should be(expectedResult2)
        menu.advancePlayerOrder should be(expectedResult3)
        menu.advancePlayerOrder should be(expectedResult4)

      }

      // ******* SHOW GAME AREA *******
      it(
        "can show the game area, including the the number of undealt cards, the trump suit card, attacking and defending cards in play, each player's hand, the amount of cards in the beaten pile, and the winner and loser"
      ) {
        Random.setSeed(42) // debugging purposes only
        val expectedResult =
          "Number of Undealt Cards:\n" +
            "36\n" +
            "Trump Suit Card:\n" +
            "JDiamond\n" +
            "\n" +
            "Attacking Player:\n" +
            "Matthew\n" +
            "Attacking Card(s) In Play:\n" +
            "\n" +
            "\n" +
            "Defending Player:\n" +
            "Mark\n" +
            "Defending Card(s) In Play:\n" +
            "\n" +
            "\n" +
            "Matthew's Hand:\n" +
            "\n" +
            "\n" +
            "Mark's Hand:\n" +
            "\n" +
            "\n" +
            "Luke's Hand:\n" +
            "\n" +
            "\n" +
            "John's Hand:\n" +
            "\n" +
            "\n" +
            "\n" +
            "Winner:\n" +
            "\n" +
            "Loser\n" +
            "\n" +
            "\n"
        val menu = new Menu
        menu.showGameArea should be(expectedResult)
      }

      // ******* CHECK FOR WINNER *******
      it("can check for a winner") {
        val expectedResult = Some("John")
        val menu = beforeEach()
        menu.initializeGame()
        menu.emptyHand // empties a player's hand for testing purposes

        menu.checkForWinner should be(expectedResult)
      }

      // ******* INITIALIZE GAME *******

      it("can initialize the game") {

        // First, set the game at some state
        val menu = beforeEach()
        menu.doMove

        val expectedResult1 =
          "Number of Undealt Cards:\n" +
            "12\n" +
            "Trump Suit Card:\n" +
            "JDiamond\n" +
            "\n" +
            "Attacking Player:\n" +
            "John\n" +
            "Attacking Card(s) In Play:\n" +
            "6Heart, 6Clover\n" +
            "\n" +
            "Defending Player:\n" +
            "Matthew\n" +
            "Defending Card(s) In Play:\n" +
            "\n" +
            "\n" +
            "Matthew's Hand:\n" +
            "6Spade, AHeart, AClover, JHeart, 7Heart, 9Heart\n" +
            "\n" +
            "Mark's Hand:\n" +
            "8Clover, KClover, QDiamond, JSpade, QSpade, 10Heart\n" +
            "\n" +
            "Luke's Hand:\n" +
            "ADiamond, 9Clover, 7Clover, 8Spade, KDiamond, JClover\n" +
            "\n" +
            "John's Hand:\n" +
            "9Diamond, 6Diamond, 8Heart, 10Diamond\n" +
            "\n" +
            "\n" +
            "Winner:\n" +
            "\n" +
            "Loser\n" +
            "\n" +
            "\n"
        menu.showGameArea should be(expectedResult1)

        // Then, test that initializing the game actually does what it promises
        menu.initializeGame()
        val expectedResult2 =
          "Number of Undealt Cards:\n" +
            "12\n" +
            "Trump Suit Card:\n" +
            "JDiamond\n" +
            "\n" +
            "Attacking Player:\n" +
            "John\n" +
            "Attacking Card(s) In Play:\n" +
            "\n" +
            "\n" +
            "Defending Player:\n" +
            "Matthew\n" +
            "Defending Card(s) In Play:\n" +
            "\n" +
            "\n" +
            "Matthew's Hand:\n" +
            "6Spade, AHeart, AClover, JHeart, 7Heart, 9Heart\n" +
            "\n" +
            "Mark's Hand:\n" +
            "8Clover, KClover, QDiamond, JSpade, QSpade, 10Heart\n" +
            "\n" +
            "Luke's Hand:\n" +
            "ADiamond, 9Clover, 7Clover, 8Spade, KDiamond, JClover\n" +
            "\n" +
            "John's Hand:\n" +
            "9Diamond, 6Diamond, 6Clover, 6Heart, 8Heart, 10Diamond\n" +
            "\n" +
            "\n" +
            "Winner:\n" +
            "\n" +
            "Loser\n" +
            "\n" +
            "\n"
        menu.showGameArea should be(expectedResult2)

      }

      // ******* DO MOVE *******

      it("can execute the default player's move (DO MOVE)") {
        // First, set the game at some state
        val menu = beforeEach()
        menu.initializeGame()

        val expectedResult1 =
          "Number of Undealt Cards:\n" +
            "12\n" +
            "Trump Suit Card:\n" +
            "JDiamond\n" +
            "\n" +
            "Attacking Player:\n" +
            "John\n" +
            "Attacking Card(s) In Play:\n" +
            "6Heart, 6Clover\n" +
            "\n" +
            "Defending Player:\n" +
            "Matthew\n" +
            "Defending Card(s) In Play:\n" +
            "\n" +
            "\n" +
            "Matthew's Hand:\n" +
            "6Spade, AHeart, AClover, JHeart, 7Heart, 9Heart\n" +
            "\n" +
            "Mark's Hand:\n" +
            "8Clover, KClover, QDiamond, JSpade, QSpade, 10Heart\n" +
            "\n" +
            "Luke's Hand:\n" +
            "ADiamond, 9Clover, 7Clover, 8Spade, KDiamond, JClover\n" +
            "\n" +
            "John's Hand:\n" +
            "9Diamond, 6Diamond, 8Heart, 10Diamond\n" +
            "\n" +
            "\n" +
            "Winner:\n" +
            "\n" +
            "Loser\n" +
            "\n" +
            "\n"

        val expectedResult2 =
          "Number of Undealt Cards:\n" +
            "12\n" +
            "Trump Suit Card:\n" +
            "JDiamond\n" +
            "\n" +
            "Attacking Player:\n" +
            "John\n" +
            "Attacking Card(s) In Play:\n" +
            "6Heart, 6Clover\n" +
            "\n" +
            "Defending Player:\n" +
            "Matthew\n" +
            "Defending Card(s) In Play:\n" +
            "7Heart, AClover\n" +
            "\n" +
            "Matthew's Hand:\n" +
            "6Spade, AHeart, JHeart, 9Heart\n" +
            "\n" +
            "Mark's Hand:\n" +
            "8Clover, KClover, QDiamond, JSpade, QSpade, 10Heart\n" +
            "\n" +
            "Luke's Hand:\n" +
            "ADiamond, 9Clover, 7Clover, 8Spade, KDiamond, JClover\n" +
            "\n" +
            "John's Hand:\n" +
            "9Diamond, 6Diamond, 8Heart, 10Diamond\n" +
            "\n" +
            "\n" +
            "Winner:\n" +
            "\n" +
            "Loser\n" +
            "\n" +
            "\n"

        val expectedResult3 =
          "Number of Undealt Cards:\n" +
            "8\n" +
            "Trump Suit Card:\n" +
            "JDiamond\n" +
            "\n" +
            "Attacking Player:\n" +
            "Matthew\n" +
            "Attacking Card(s) In Play:\n" +
            "6Spade\n" +
            "\n" +
            "Defending Player:\n" +
            "Mark\n" +
            "Defending Card(s) In Play:\n" +
            "\n" +
            "\n" +
            "Matthew's Hand:\n" +
            "AHeart, JHeart, 9Heart, 7Spade, 9Spade\n" +
            "\n" +
            "Mark's Hand:\n" +
            "8Clover, KClover, QDiamond, JSpade, QSpade, 10Heart\n" +
            "\n" +
            "Luke's Hand:\n" +
            "ADiamond, 9Clover, 7Clover, 8Spade, KDiamond, JClover\n" +
            "\n" +
            "John's Hand:\n" +
            "9Diamond, 6Diamond, 8Heart, 10Diamond, QHeart, KSpade\n" +
            "\n" +
            "\n" +
            "Winner:\n" +
            "\n" +
            "Loser\n" +
            "\n" +
            "\n"

        val expectedResult4 =
          "Number of Undealt Cards:\n" +
            "8\n" +
            "Trump Suit Card:\n" +
            "JDiamond\n" +
            "\n" +
            "Attacking Player:\n" +
            "Matthew\n" +
            "Attacking Card(s) In Play:\n" +
            "6Spade\n" +
            "\n" +
            "Defending Player:\n" +
            "Mark\n" +
            "Defending Card(s) In Play:\n" +
            "JSpade\n" +
            "\n" +
            "Matthew's Hand:\n" +
            "AHeart, JHeart, 9Heart, 7Spade, 9Spade\n" +
            "\n" +
            "Mark's Hand:\n" +
            "8Clover, KClover, QDiamond, QSpade, 10Heart\n" +
            "\n" +
            "Luke's Hand:\n" +
            "ADiamond, 9Clover, 7Clover, 8Spade, KDiamond, JClover\n" +
            "\n" +
            "John's Hand:\n" +
            "9Diamond, 6Diamond, 8Heart, 10Diamond, QHeart, KSpade\n" +
            "\n" +
            "\n" +
            "Winner:\n" +
            "\n" +
            "Loser\n" +
            "\n" +
            "\n"

        val expectedResult5 =
          "Number of Undealt Cards:\n" +
            "6\n" +
            "Trump Suit Card:\n" +
            "JDiamond\n" +
            "\n" +
            "Attacking Player:\n" +
            "Mark\n" +
            "Attacking Card(s) In Play:\n" +
            "8Clover\n" +
            "\n" +
            "Defending Player:\n" +
            "Luke\n" +
            "Defending Card(s) In Play:\n" +
            "\n" +
            "\n" +
            "Matthew's Hand:\n" +
            "AHeart, JHeart, 9Heart, 7Spade, 9Spade, ASpade\n" +
            "\n" +
            "Mark's Hand:\n" +
            "KClover, QDiamond, QSpade, 10Heart, QClover\n" +
            "\n" +
            "Luke's Hand:\n" +
            "ADiamond, 9Clover, 7Clover, 8Spade, KDiamond, JClover\n" +
            "\n" +
            "John's Hand:\n" +
            "9Diamond, 6Diamond, 8Heart, 10Diamond, QHeart, KSpade\n" +
            "\n" +
            "\n" +
            "Winner:\n" +
            "\n" +
            "Loser\n" +
            "\n" +
            "\n"

        menu.doMove should be(expectedResult1)
        menu.doMove should be(expectedResult2)
        menu.doMove should be(expectedResult3)
        menu.doMove should be(expectedResult4)
        menu.doMove should be(expectedResult5)
      }

      it("can execute the Trump Hoarder player's move (DO MOVE)") {
        // First, set the game at some state
        val menu = beforeEach()

        val expectedResult1 =
          "Number of Undealt Cards:\n" +
            "12\n" +
            "Trump Suit Card:\n" +
            "JDiamond\n" +
            "\n" +
            "Attacking Player:\n" +
            "John\n" +
            "Attacking Card(s) In Play:\n" +
            "6Heart, 6Clover\n" +
            "\n" +
            "Defending Player:\n" +
            "Matthew\n" +
            "Defending Card(s) In Play:\n" +
            "\n" +
            "\n" +
            "Matthew's Hand:\n" +
            "6Spade, AHeart, AClover, JHeart, 7Heart, 9Heart\n" +
            "\n" +
            "Mark's Hand:\n" +
            "8Clover, KClover, QDiamond, JSpade, QSpade, 10Heart\n" +
            "\n" +
            "Luke's Hand:\n" +
            "ADiamond, 9Clover, 7Clover, 8Spade, KDiamond, JClover\n" +
            "\n" +
            "John's Hand:\n" +
            "9Diamond, 6Diamond, 8Heart, 10Diamond\n" +
            "\n" +
            "\n" +
            "Winner:\n" +
            "\n" +
            "Loser\n" +
            "\n" +
            "\n"

        val expectedResult2 =
          "Number of Undealt Cards:\n" +
            "12\n" +
            "Trump Suit Card:\n" +
            "JDiamond\n" +
            "\n" +
            "Attacking Player:\n" +
            "John\n" +
            "Attacking Card(s) In Play:\n" +
            "6Heart, 6Clover\n" +
            "\n" +
            "Defending Player:\n" +
            "Matthew\n" +
            "Defending Card(s) In Play:\n" +
            "7Heart, AClover\n" +
            "\n" +
            "Matthew's Hand:\n" +
            "6Spade, AHeart, JHeart, 9Heart\n" +
            "\n" +
            "Mark's Hand:\n" +
            "8Clover, KClover, QDiamond, JSpade, QSpade, 10Heart\n" +
            "\n" +
            "Luke's Hand:\n" +
            "ADiamond, 9Clover, 7Clover, 8Spade, KDiamond, JClover\n" +
            "\n" +
            "John's Hand:\n" +
            "9Diamond, 6Diamond, 8Heart, 10Diamond\n" +
            "\n" +
            "\n" +
            "Winner:\n" +
            "\n" +
            "Loser\n" +
            "\n" +
            "\n"

        val expectedResult22 =
          "Number of Undealt Cards:\n" +
            "12\n" +
            "Trump Suit Card:\n" +
            "JDiamond\n" +
            "\n" +
            "Attacking Player:\n" +
            "John\n" +
            "Attacking Card(s) In Play:\n" +
            "8Heart, 6Heart, 6Clover\n" + 
            "\n" +
            "Defending Player:\n" +
            "Matthew\n" +
            "Defending Card(s) In Play:\n" +
            "7Heart, AClover\n" +
            "\n" +
            "Matthew's Hand:\n" +
            "6Spade, AHeart, JHeart, 9Heart\n" +
            "\n" +
            "Mark's Hand:\n" +
            "8Clover, KClover, QDiamond, JSpade, QSpade, 10Heart\n" +
            "\n" +
            "Luke's Hand:\n" +
            "ADiamond, 9Clover, 7Clover, 8Spade, KDiamond, JClover\n" +
            "\n" +
            "John's Hand:\n" +
            "9Diamond, 6Diamond, 10Diamond\n" +
            "\n" +
            "\n" +
            "Winner:\n" +
            "\n" +
            "Loser\n" +
            "\n" +
            "\n"

        val expectedResult23 =
          "Number of Undealt Cards:\n" +
            "12\n" +
            "Trump Suit Card:\n" +
            "JDiamond\n" +
            "\n" +
            "Attacking Player:\n" +
            "John\n" +
            "Attacking Card(s) In Play:\n" +
            "8Heart, 6Heart, 6Clover\n" +
            "\n" +
            "Defending Player:\n" +
            "Matthew\n" +
            "Defending Card(s) In Play:\n" +
            "7Heart, AClover, 9Heart\n" +
            "\n" +
            "Matthew's Hand:\n" +
            "6Spade, AHeart, JHeart\n" +
            "\n" +
            "Mark's Hand:\n" +
            "8Clover, KClover, QDiamond, JSpade, QSpade, 10Heart\n" +
            "\n" +
            "Luke's Hand:\n" +
            "ADiamond, 9Clover, 7Clover, 8Spade, KDiamond, JClover\n" +
            "\n" +
            "John's Hand:\n" +
            "9Diamond, 6Diamond, 10Diamond\n" +
            "\n" +
            "\n" +
            "Winner:\n" +
            "\n" +
            "Loser\n" +
            "\n" +
            "\n"

        val expectedResult3 =
          "Number of Undealt Cards:\n" +
            "6\n" +
            "Trump Suit Card:\n" +
            "JDiamond\n" +
            "\n" +
            "Attacking Player:\n" +
            "Matthew\n" +
            "Attacking Card(s) In Play:\n" +
            "6Spade\n" +
            "\n" +
            "Defending Player:\n" +
            "Mark\n" +
            "Defending Card(s) In Play:\n" +
            "\n" +
            "\n" +
            "Matthew's Hand:\n" +
            "AHeart, JHeart, 9Spade, ASpade, QClover\n" +
            "\n" +
            "Mark's Hand:\n" +
            "8Clover, KClover, QDiamond, JSpade, QSpade, 10Heart\n" +
            "\n" +
            "Luke's Hand:\n" +
            "ADiamond, 9Clover, 7Clover, 8Spade, KDiamond, JClover\n" +
            "\n" +
            "John's Hand:\n" +
            "9Diamond, 6Diamond, 10Diamond, QHeart, KSpade, 7Spade\n" +
            "\n" +
            "\n" +
            "Winner:\n" +
            "\n" +
            "Loser\n" +
            "\n" +
            "\n"

        val expectedResult32 =
          "Number of Undealt Cards:\n" +
            "6\n" +
            "Trump Suit Card:\n" +
            "JDiamond\n" +
            "\n" +
            "Attacking Player:\n" +
            "Matthew\n" +
            "Attacking Card(s) In Play:\n" +
            "6Spade\n" +
            "\n" +
            "Defending Player:\n" +
            "Mark\n" +
            "Defending Card(s) In Play:\n" +
            "JSpade\n" +
            "\n" +
            "Matthew's Hand:\n" +
            "AHeart, JHeart, 9Spade, ASpade, QClover\n" +
            "\n" +
            "Mark's Hand:\n" +
            "8Clover, KClover, QDiamond, QSpade, 10Heart\n" +
            "\n" +
            "Luke's Hand:\n" +
            "ADiamond, 9Clover, 7Clover, 8Spade, KDiamond, JClover\n" +
            "\n" +
            "John's Hand:\n" +
            "9Diamond, 6Diamond, 10Diamond, QHeart, KSpade, 7Spade\n" +
            "\n" +
            "\n" +
            "Winner:\n" +
            "\n" +
            "Loser\n" +
            "\n" +
            "\n"

        val expectedResult33 =
          "Number of Undealt Cards:\n" +
            "8\n" +
            "Trump Suit Card:\n" +
            "JDiamond\n" +
            "\n" +
            "Attacking Player:\n" +
            "Matthew\n" +
            "Attacking Card(s) In Play:\n" +
            "6Spade, 9Spade, ASpade\n" +
            "\n" +
            "Defending Player:\n" +
            "Mark\n" +
            "Defending Card(s) In Play:\n" +
            "JSpade\n" +
            "\n" +
            "Matthew's Hand:\n" +
            "AHeart, JHeart, AClover\n" +
            "\n" +
            "Mark's Hand:\n" +
            "8Clover, KClover, QDiamond, QSpade, 10Heart\n" +
            "\n" +
            "Luke's Hand:\n" +
            "ADiamond, 9Clover, 7Clover, 8Spade, KDiamond, JClover\n" +
            "\n" +
            "John's Hand:\n" +
            "9Diamond, 6Diamond, 10Diamond, QHeart, KCLover, 7Spade\n" +
            "\n" +
            "\n" +
            "Winner:\n" +
            "\n" +
            "Loser\n" +
            "\n" +
            "\n"

        for player <- menu.getPlayers() do
          menu.setStrategy("trump hoarder", player)

        menu.doMove should be(expectedResult1)
        menu.doMove should be(expectedResult2)
        menu.doMove should be(expectedResult22)
        menu.doMove should be(expectedResult23)
        menu.doMove should be(expectedResult3)
        menu.doMove should be(expectedResult32)
        // menu.doMove should be(expectedResult33)
        // suggestion... test with should contain
        // with the lines that are changing rather
        // than the entire string over and over
      }

      it("can execute the Aggressive Attacker player's move (DO MOVE)") {
        // First, set the game at some state
        val menu = beforeEach()

        val expectedResult1 =
          "Number of Undealt Cards:\n" +
            "12\n" +
            "Trump Suit Card:\n" +
            "JDiamond\n" +
            "\n" +
            "Attacking Player:\n" +
            "John\n" +
            "Attacking Card(s) In Play:\n" +
            "10Diamond\n" +
            "\n" +
            "Defending Player:\n" +
            "Matthew\n" +
            "Defending Card(s) In Play:\n" +
            "\n" +
            "\n" +
            "Matthew's Hand:\n" +
            "6Spade, AHeart, AClover, JHeart, 7Heart, 9Heart\n" +
            "\n" +
            "Mark's Hand:\n" +
            "8Clover, KClover, QDiamond, JSpade, QSpade, 10Heart\n" +
            "\n" +
            "Luke's Hand:\n" +
            "ADiamond, 9Clover, 7Clover, 8Spade, KDiamond, JClover\n" +
            "\n" +
            "John's Hand:\n" +
            "9Diamond, 6Diamond, 6Clover, 6Heart, 8Heart\n" +
            "\n" +
            "\n" +
            "Winner:\n" +
            "\n" +
            "Loser\n" +
            "\n" +
            "\n"

        val expectedResult2 =
          "Number of Undealt Cards:\n" +
            "11\n" +
            "Trump Suit Card:\n" +
            "JDiamond\n" +
            "\n" +
            "Attacking Player:\n" +
            "Mark\n" +
            "Attacking Card(s) In Play:\n" +
            "QDiamond\n" +
            "\n" +
            "Defending Player:\n" +
            "Luke\n" +
            "Defending Card(s) In Play:\n" +
            "\n" +
            "\n" +
            "Matthew's Hand:\n" +
            "6Spade, AHeart, AClover, JHeart, 7Heart, 9Heart, 10Diamond\n" +
            "\n" +
            "Mark's Hand:\n" +
            "8Clover, KClover, JSpade, QSpade, 10Heart\n" +
            "\n" +
            "Luke's Hand:\n" +
            "ADiamond, 9Clover, 7Clover, 8Spade, KDiamond, JClover\n" +
            "\n" +
            "John's Hand:\n" +
            "9Diamond, 6Diamond, 6Clover, 6Heart, 8Heart, QHeart\n" +
            "\n" +
            "\n" +
            "Winner:\n" +
            "\n" +
            "Loser\n" +
            "\n" +
            "\n"

        val expectedResult3 =
          "Number of Undealt Cards:\n" +
            "11\n" +
            "Trump Suit Card:\n" +
            "JDiamond\n" +
            "\n" +
            "Attacking Player:\n" +
            "Mark\n" +
            "Attacking Card(s) In Play:\n" +
            "QDiamond\n" +
            "\n" +
            "Defending Player:\n" +
            "Luke\n" +
            "Defending Card(s) In Play:\n" +
            "KDiamond\n" +
            "\n" +
            "Matthew's Hand:\n" +
            "6Spade, AHeart, AClover, JHeart, 7Heart, 9Heart, 10Diamond\n" +
            "\n" +
            "Mark's Hand:\n" +
            "8Clover, KClover, JSpade, QSpade, 10Heart\n" +
            "\n" +
            "Luke's Hand:\n" +
            "ADiamond, 9Clover, 7Clover, 8Spade, JClover\n" +
            "\n" +
            "John's Hand:\n" +
            "9Diamond, 6Diamond, 6Clover, 6Heart, 8Heart, QHeart\n" +
            "\n" +
            "\n" +
            "Winner:\n" +
            "\n" +
            "Loser\n" +
            "\n" +
            "\n"

        val expectedResult33 =
          "Number of Undealt Cards:\n" +
            "8\n" +
            "Trump Suit Card:\n" +
            "JDiamond\n" +
            "\n" +
            "Attacking Player:\n" +
            "John\n" +
            "Attacking Card(s) In Play:\n" +
            "9Diamond\n" +
            "\n" +
            "Defending Player:\n" +
            "Matthew\n" +
            "Defending Card(s) In Play:\n" +
            "\n" +
            "\n" +
            "Matthew's Hand:\n" +
            "6Spade, AHeart, AClover, JHeart, 7Heart, 9Heart, 10Diamond\n" +
            "\n" +
            "Mark's Hand:\n" +
            "8Clover, JSpade, 10Heart, KSpade, 7Spade, 9Spade\n" +
            "\n" +
            "Luke's Hand:\n" +
            "ADiamond, 9Clover, 7Clover, 8Spade, JClover, QSpade, KClover, QDiamond, KDiamond\n" +
            "\n" +
            "John's Hand:\n" +
            "6Diamond, 6Clover, 6Heart, 8Heart, QHeart\n" +
            "\n" +
            "\n" +
            "Winner:\n" +
            "\n" +
            "Loser\n" +
            "\n" +
            "\n"

        val expectedResult32 =
          "Number of Undealt Cards:\n" +
            "11\n" +
            "Trump Suit Card:\n" +
            "JDiamond\n" +
            "\n" +
            "Attacking Player:\n" +
            "Mark\n" +
            "Attacking Card(s) In Play:\n" +
            "QSpade, QDiamond, KClover\n" +
            "\n" +
            "Defending Player:\n" +
            "Luke\n" +
            "Defending Card(s) In Play:\n" +
            "KDiamond\n" +
            "\n" +
            "Matthew's Hand:\n" +
            "6Spade, AHeart, AClover, JHeart, 7Heart, 9Heart, 10Diamond\n" +
            "\n" +
            "Mark's Hand:\n" +
            "8Clover, JSpade, 10Heart\n" +
            "\n" +
            "Luke's Hand:\n" +
            "ADiamond, 9Clover, 7Clover, 8Spade, JClover\n" +
            "\n" +
            "John's Hand:\n" +
            "9Diamond, 6Diamond, 6Clover, 6Heart, 8Heart, QHeart\n" +
            "\n" +
            "\n" +
            "Winner:\n" +
            "\n" +
            "Loser\n" +
            "\n" +
            "\n"

        val expectedResult34 =
            "Number of Undealt Cards:\n" +
            "8\n" +
            "Trump Suit Card:\n" +
            "JDiamond\n" +
            "\n" +
            "Attacking Player:\n" +
            "John\n" +
            "Attacking Card(s) In Play:\n" +
            "9Diamond\n" +
            "\n" +
            "Defending Player:\n" +
            "Matthew\n" +
            "Defending Card(s) In Play:\n" +
            "10Diamond\n" +
            "\n" +
            "Matthew's Hand:\n" +
            "6Spade, AHeart, AClover, JHeart, 7Heart, 9Heart\n" +
            "\n" +
            "Mark's Hand:\n" +
            "8Clover, JSpade, 10Heart, KSpade, 7Spade, 9Spade\n" +
            "\n" +
            "Luke's Hand:\n" +
            "ADiamond, 9Clover, 7Clover, 8Spade, JClover, QSpade, KClover, QDiamond, KDiamond\n" +
            "\n" +
            "John's Hand:\n" +
            "6Diamond, 6Clover, 6Heart, 8Heart, QHeart\n" +
            "\n" +
            "\n" +
            "Winner:\n" +
            "\n" +
            "Loser\n" +
            "\n" +
            "\n"

        for player <- menu.getPlayers() do
          menu.setStrategy("aggressive", player)

        menu.doMove should be(expectedResult1)
        menu.doMove should be(expectedResult2)
        menu.doMove should be(expectedResult3)
        menu.doMove should be(expectedResult32)
        menu.doMove should be(expectedResult33)
        menu.doMove should be(expectedResult34)
      }

      // ******* DO TURN *******

      it("can show a turn (four Do Moves)") {
        val expectedResult =
          "Number of Undealt Cards:\n" +
            "8\n" +
            "Trump Suit Card:\n" +
            "JDiamond\n" +
            "\n" +
            "Attacking Player:\n" +
            "Matthew\n" +
            "Attacking Card(s) In Play:\n" +
            "6Spade\n" +
            "\n" +
            "Defending Player:\n" +
            "Mark\n" +
            "Defending Card(s) In Play:\n" +
            "JSpade\n" +
            "\n" +
            "Matthew's Hand:\n" +
            "AHeart, JHeart, 9Heart, 7Spade, 9Spade\n" +
            "\n" +
            "Mark's Hand:\n" +
            "8Clover, KClover, QDiamond, QSpade, 10Heart\n" +
            "\n" +
            "Luke's Hand:\n" +
            "ADiamond, 9Clover, 7Clover, 8Spade, KDiamond, JClover\n" +
            "\n" +
            "John's Hand:\n" +
            "9Diamond, 6Diamond, 8Heart, 10Diamond, QHeart, KSpade\n" +
            "\n" +
            "\n" +
            "Winner:\n" +
            "\n" +
            "Loser\n" +
            "\n" +
            "\n"

        val menu = beforeEach()
        menu.doTurn should be(expectedResult)
      }
      it("can play the game until the game is won") {
        val expectedResult = "Number of Undealt Cards:\n" +
          "0\n" +
          "Trump Suit Card:\n" +
          "JDiamond\n" +
          "\n" +
          "Attacking Player:\n" +
          "Mark\n" +
          "Attacking Card(s) In Play:\n" +
          "KClover\n" +
          "\n" +
          "Defending Player:\n" +
          "Luke\n" +
          "Defending Card(s) In Play:\n" +
          "\n" +
          "\n" +
          "Matthew's Hand:\n" +
          "JDiamond\n" +
          "\n" +
          "Mark's Hand:\n" +
          "\n" +
          "\n" +
          "Luke's Hand:\n" +
          "ADiamond, JClover\n" +
          "\n" +
          "John's Hand:\n" +
          "9Diamond, 10Diamond\n" +
          "\n" +
          "\n" +
          "Winner:Mark\n" +
          "\n" +
          "Loser\n" +
          "\n" +
          "\n"

        beforeEach().doGame should be(expectedResult)
      }

      // ******* SET STRATEGY *******
      it("can set a player strategy") {
        val menu = beforeEach()

        val players = menu.getPlayerOrder.get_player_lineup().toList

        val expectedResult1 = 
          players(0).get_name() + "'s strategy is default"
        val expectedResult2 =
          players(1).get_name() + "'s strategy is trump hoarder"
        val expectedResult3 =
          players(2).get_name() + "'s strategy is chaotic player"
        val expectedResult4 =
          players(3).get_name() + "'s strategy is aggressive attacker"

        menu.setStrategy("default", players(0)) should be(expectedResult1)
        menu.setStrategy("trump hoarder", players(1)) should be(expectedResult2)
        menu.setStrategy("chaotic player", players(2)) should be(expectedResult3)
        menu.setStrategy("aggressive attacker", players(3)) should be(expectedResult4)

      }

      it("can display all players' strategies") {
        val menu = new Menu
        val players = menu.getPlayerOrder.get_player_lineup().toList

        val expectedResult1 = 
          players(0).get_name() + "'s strategy is default"
        val expectedResult2 =
          players(1).get_name() + "'s strategy is trump hoarder"
        val expectedResult3 =
          players(2).get_name() + "'s strategy is chaotic player"
        val expectedResult4 =
          players(3).get_name() + "'s strategy is aggressive attacker"

        menu.setStrategy("default", players(0))
        menu.setStrategy("trump hoarder", players(1))
        menu.setStrategy("chaotic player", players(2))
        menu.setStrategy("aggressive attacker", players(3))

        menu.showStrategies() should be(
          expectedResult1 + "\n" +
            expectedResult2 + "\n" +
            expectedResult3 + "\n" +
            expectedResult4 + "\n"
        )


      }
    }

  }

}
