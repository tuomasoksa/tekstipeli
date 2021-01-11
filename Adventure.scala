package o1.adventure
import scala.io.StdIn.readLine
//import java.awt.geom.Area



/** The class `Adventure` represents text adventure games. An adventure consists of a player and
  * a number of areas that make up the game world. It provides methods for playing the game one
  * turn at a time and for checking the state of the game.
  *
  * N.B. This version of the class has a lot of "hard-coded" information which pertain to a very
  * specific adventure game that involves a small trip through a twisted forest. All newly created
  * instances of class `Adventure` are identical to each other. To create other kinds of adventure
  * games, you will need to modify or replace the source code of this class. */
class Adventure {

  /** The title of the adventure game. */
  val title = "Trump vs. Biden"

  private val middle      = new Area("Arizona", "Olet keskellä aavikkoa. Ympärillä ei näy ristin sielua.\nKitukasvuisessa puussa lymyää kameleontti.")
  private val pohjoisCarolina = new Area("Pohjois-Carolina", "Hirveä kaatosade. Siitä huolimatta Trumpin kannattajia on joka puolella.\nPunaisia make America great again lippiksiä on ihmisten päässä.\nKaukana näkyy yksinäinen alpakka syömässä rehua.")
  private val georgia = new Area("Georgia", "Bidenin kannattajia on kaikkialla. Aurinko porottaa siniseltä taivaalta.\nVoittaako demokraattien edustaja ensimmäistä kertaa 30 vuoteen tämän osavaltion valitsijamiehet?")
  private val pennsylvania    = new Area("Pennsylvania", "Olet vuoriston juurella. Kaukana näkyy ihmisiä osoittamassa mieltään.\n20 valitsijamiehen osavaltio on tärkeässä asemassa vaaleissa.")
  private val newYork      = new Area("New York", "Olet New Yorkin metropolialueella.\nIhmisiä on valtavasti ympärillä. Pelkkä suojatien ylitys tuottaa vaikeuksia.")
  private val whiteHouse  = new Area("White House", "Saavuit Washingtoniin. Auringonlasku valaisee hiekkakivestä rakennettua kartanoa! Tarvitset enää kateissa olevan äänestyslapun!")
  private val destination = whiteHouse
  private val song2 = "<<abc--"
  private val song3 = ">>[89]aaaaaahhhhhhhhh--"

       middle.setNeighbors(Vector("pohjoiseen" -> pohjoisCarolina, "itään" -> newYork, "etelään" -> georgia, "länteen" -> pennsylvania   ))
  pohjoisCarolina.setNeighbors(Vector(                        "itään" -> newYork, "etelään" -> middle,      "länteen" -> pennsylvania   ))
  georgia.setNeighbors(Vector("pohjoiseen" -> middle,      "itään" -> newYork, "etelään" -> georgia, "länteen" -> pennsylvania   ))
     pennsylvania.setNeighbors(Vector("pohjoiseen" -> pohjoisCarolina, "itään" -> middle, "etelään" -> georgia, "länteen" -> pohjoisCarolina))
       newYork.setNeighbors(Vector("pohjoiseen" -> pohjoisCarolina, "itään" -> whiteHouse,   "etelään" -> georgia, "länteen" -> pohjoisCarolina))
         whiteHouse.setNeighbors(Vector(                                                                  "länteen" -> newYork     ))

  // TODO: place these two items in clearing and southForest, respectively

  pennsylvania.addItem(new Item("äänestyslappu", "Tämä äänestyslappu määrää Yhdysvaltojen suraavan presidentin!"))
  georgia.addItem(new Item("puhelin", "Näyttää täysin toimivalta puhelimelta.\nTästähän voisi olla hyötyä"))


  /** The character that the player controls in the game. */
  val player = new Player(middle, Adventure.this)
  val riot = new Riot(whiteHouse)

  /** The number of turns that have passed since the start of the game. */
  var turnCount = 0
  /** The maximum number of turns that this adventure game allows before time runs out. */
  val timeLimit = 20


  /** Determines if the adventure is complete, that is, if the player has won. */
  def isComplete = player.has("äänestyslappu") && (this.player.location == this.destination)

  /** Determines whether the player has won, lost, or quit, thereby ending the game. */
  def isOver = this.isComplete || this.player.hasQuit || player.siirrot >= this.timeLimit

  /** Returns a message that is to be displayed to the player at the beginning of the game. */
  def welcomeMessage = "Yhdysvaltain 59. presidentinvaalit ovat alkaneet!!!\nDonald J. Trump vs. Joseph R. Biden on kutkuttavassa tilanteessa.\nÄänestys on tasatilanteessa, mutta " +
    "yksi äänestyslappu on kadonnut!\nLöydä se ja palaa White Houseen kertomaan lopputulos maailmalle, ennen kuin on liian myöhäistä!"


  /** Returns a message that is to be displayed to the player at the end of the game. The message
    * will be different depending on whether or not the player has completed their quest. */
  def goodbyeMessage = {
    if (this.isComplete) {
      o1.play(song3)
      println("\n\nOlet saapunut White Houseen ajallaan, onnittelut!!\nVoit nyt katsoa mitä äänestyslapussa lukee.\n\nKerro koko maailmalle kuka Yhdysvaltojen seuraava presidentti on kirjoittamalla joko biden tai trump!")
      var voittaja = readLine()
      while (voittaja != "trump" && voittaja != "biden"){
        println("Kirjoita joko biden tai trump")
        voittaja = readLine()
      }
      if (voittaja == "trump") "\n\nYhdysvaltojen seuraava presidentti on DONALD J. TRUMP.\nKEEP AMERICA GREAT!\nIhmiset villiintyvät ympäri maailmaa ja juhlat ovat alkaneet."
      else "\n\nYhdysvaltojen seuraava presidentti on JOSEPH R. BIDEN.\nOUR BEST DAYS STILL LIE AHEAD!\nIhmiset villiintyvät ympäri maailmaa ja juhlat ovat alkaneet."

    } else if (player.siirrot >= this.timeLimit) {
      o1.play(song2)
      "EIKÄ! Aika on loppu, etkä pystynyt ratkaisemaan vaaleja.\nIhmiset mellakoivat ja osoittavat mieltään, katastrofin ainekset ovat käsillä.\nGame over!"
    } else  // game over due to player quitting
      "Luovuttaja!"
  }


  /** Plays a turn by executing the given in-game command, such as "go west". Returns a textual
    * report of what happened, or an error message if the command was unknown. In the latter
    * case, no turns elapse. */
  def playTurn(command: String) = {
    val action = new Action(command)
    val outcomeReport = action.execute(this.player)
    if (outcomeReport.isDefined) {
      riot.liiku
      this.turnCount += 1
    }
    outcomeReport.getOrElse("Epäselvä käsky: \"" + command + "\".") + (if (riot.currentLocationRiot == player.currentLocation) {
      this.turnCount += 1
      player.siirrot += 1
      "\n\nKohtasit mellakoitsijoita, jotka hidastivat matkantekoasi.\nNyt sinulla on entistä kovempi kiire!!"
    }
    else "...")


  }


}

