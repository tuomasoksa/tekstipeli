package o1.adventure


/** The class `Action` represents actions that a player may take in a text adventure game.
  * `Action` objects are constructed on the basis of textual commands and are, in effect,
  * parsers for such commands. An action object is immutable after creation.
  * @param input  a textual in-game command such as "go east" or "rest" */
class Action(input: String) {

  private val commandText = input.trim.toLowerCase
  private val verb        = commandText.takeWhile( _ != ' ' )
  private val modifiers   = commandText.drop(verb.length).trim


  /** Causes the given player to take the action represented by this object, assuming
    * that the command was understood. Returns a description of what happened as a result
    * of the action (such as "You go west."). The description is returned in an `Option`
    * wrapper; if the command was not recognized, `None` is returned. */
  def execute(actor: Player) = this.verb match {
    case "mene"    => Some(actor.mene(this.modifiers))
    case "tauko"  => Some(actor.tauko())
    case "luovuta"  => Some(actor.luovuta())
    case "tutki"   => Some(actor.tutki(modifiers))
    case "pudota" => Some(actor.pudota(modifiers))
    case "esineet" => Some(actor.esineet)
    case "ota" => Some(actor.ota(modifiers))
    case "soita" => Some(actor.soita)
    case "help" => Some(actor.help)
    case "vihje" => Some(actor.vihje)
    case "mellakka" => Some(actor.mellakka)
    case other   => None
  }

  def execute(actor: Riot) = this.verb match {
    case "liiku" => Some(actor.liiku)

  }
  /** Returns a textual description of the action object, for debugging purposes. */
  override def toString = this.verb + " (modifiers: " + this.modifiers + ")"


}

