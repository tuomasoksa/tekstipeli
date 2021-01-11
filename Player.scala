package o1.adventure

import scala.collection.mutable.Map


/** A `Player` object represents a player character controlled by the real-life user of the program.
  *
  * A player object's state is mutable: the player's location and possessions can change, for instance.
  *
  * @param startingArea  the initial location of the player */
class Player(startingArea: Area, adventure: Adventure) {

  var currentLocation = startingArea        // gatherer: changes in relation to the previous location
  private var quitCommandGiven = false              // one-way flag
  var siirrot = 0
  private val song = "ach---"
  private val sijainnit = Map("New York" -> "siirtyä länteen", "Arizona" -> "siirtyä länteen", "Pohjois-Carolina" -> "siirtyä etelään", "Georgia" -> "siirtyä länteen", "Pennsylvania" -> "katsoa ympärille", "White House" -> "siirtyä länteen")
  private val sijainnit2 = Map("New York" -> "siiryä itään", "Arizona" -> "siirtyä itään", "Pohjois-Carolina" -> "siirtyä itään", "Georgia" -> "siirtyä itään", "Pennsylvania" -> "siirtyä itään", "White House" -> "Olet White Housessa")
  private val esineet2 = Map[String, Item]()

  def pudota(itemName: String): String = {
    if (esineet2.contains(itemName)) {
       val testi = esineet2.remove(itemName)
       currentLocation.addItem(testi.getOrElse(new Item("testi", "testi")))
       itemName + " pudotettu maahan"
    }
    else "Sinulla ei ole sitä!"
  }
  def tutki(itemName: String): String = {
    if (esineet2.contains(itemName)) {
        val kuvaus = esineet2.get(itemName) match {
        case Some(x) => x.description
        case None => itemName
      }
      "Siinähän on " + esineet2.getOrElse(itemName, None) + ".\n" + kuvaus
    }
    else "Jos haluat tutkia jotain, sinun pitää ensin löytää kyseinen esine!"
  }
  def ota(itemName: String): String = {
      currentLocation.removeItem(itemName) match {
        case Some(x) =>
          esineet2(itemName) = x
          o1.play(song)
          "Sinulla on " + x + "."
        case None => itemName + " ei ole saatavilla"
      }
    }

  def esineet: String = {
    if (esineet2.nonEmpty) "Sinulla on: \n" + esineet2.keys.mkString("\n")
    else "Olet tyhjin käsin"
  }
  def soita = {
    if (esineet2.contains("puhelin")) {
      "\nWhite Housesta kerrotaan puhelinvälityksellä, että sinulla on " + (20 - siirrot) + " tuntia aikaa löytää äänestyslappu."
    }
    else "Sinulla ei ole puhelinta"
  }
  def has(itemName: String): Boolean = esineet2.contains(itemName)
  def help: String = "Tavoitteena on löytää äänestyslappu ja palauttaa se White Houseen ennen kuin aika loppuu.\nVoit siirtyä eri kohteisiin komennolla: mene ilmansuuntaan. Esimerkiksi mene länteen.\nKomennolla: soita, saat jäljellä olevan ajan tietoosi. Tähän tarvitset puhelimen."+
    "\nKomennolla: ota esineenNimi, voit poimia maasta esineen. Esimerkiksi ota puhelin.\nJos olet jumissa, voit pyytää apua komennolla vihje.\nVoit suunnitella rettisi paremmin käyttämällä komentoa mellakka, joka kertoo mellakoitsijoiden sijainnin." +
    "\nVoit kokeilla myös komentoja: pudota esineenNimi, tutki esineenNimi, esineet, tauko." +
    "\nVaro mellakoita, ne hidastavat matkaasi vähentämällä jäljellä olevaa aikaa"


  /** Determines if the player has indicated a desire to quit the game. */
  def hasQuit = this.quitCommandGiven


  /** Returns the current location of the player. */
  def location = this.currentLocation


  /** Attempts to move the player in the given direction. This is successful if there
    * is an exit from the player's current location towards the direction name. Returns
    * a description of the result: "You go DIRECTION." or "You can't go DIRECTION." */
  def mene(direction: String) = {
    val destination = this.location.neighbor(direction)
    this.currentLocation = destination.getOrElse(this.currentLocation)
    if (destination.isDefined) {

      siirrot += 1
      "Siirryit " + direction + "."
    }
    else "Et voi siirtyä " + direction + "."
  }
  def vihje: String = {
    var suunta = ""
    for (sijainti <- sijainnit) {
      if (sijainti._1 == currentLocation.name) {
        if (!esineet2.contains("äänestyslappu"))
        suunta = "Löytääksesi äänestyslapun, sinun kannattaa " + sijainti._2
        else suunta = "Päästäksesi White Houseen, sinun kannattaa " + sijainnit2.getOrElse(sijainti._1, "")
      }
    }
    suunta
  }

  def mellakka = {
    "\n\nMellakoitsijat ovat tällä hetkellä " + adventure.riot.currentLocationRiot.name + "ssa"
  }

  /** Causes the player to rest for a short while (this has no substantial effect in game terms).
    * Returns a description of what happened. */
  def tauko() = {
    "Lepäät hetken, muista että aika juoksee"
  }


  /** Signals that the player wants to quit the game. Returns a description of what happened within
    * the game as a result (which is the empty string, in this case). */
  def luovuta() = {
    this.quitCommandGiven = true
    ""
  }


  /** Returns a brief description of the player's state, for debugging purposes. */
  override def toString = "Sijaintisi on: " + this.location.name


}


