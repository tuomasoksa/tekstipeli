package o1.adventure

import scala.util.Random

class Riot(startingArea: Area) {

  var currentLocationRiot = startingArea
  private var suunnat = Vector("itään", "länteen", "pohjoiseen", "etelään")


  def liiku = {
    def random: Int = scala.util.Random.nextInt(4)
    val maaranpaa = this.currentLocationRiot.neighbor(suunnat(random))
    this.currentLocationRiot = maaranpaa.getOrElse(this.currentLocationRiot)
    if (maaranpaa.isDefined) {
      "Mielenosoitus siirtyi " + maaranpaa + "."
    }
  }
}
