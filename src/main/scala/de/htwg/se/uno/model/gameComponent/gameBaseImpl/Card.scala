package de.htwg.se.uno.model.gameComponent.gameBaseImpl

case class Card(color: Color, value: Value) {
  override def toString:String = {
    value match {
      case Value.Zero => "" + color.toString.charAt(0) + " 0"
      case Value.One => "" + color.toString.charAt(0) + " 1"
      case Value.Two => "" + color.toString.charAt(0) + " 2"
      case Value.Three => "" + color.toString.charAt(0) + " 3"
      case Value.Four => "" + color.toString.charAt(0) + " 4"
      case Value.Five => "" + color.toString.charAt(0) + " 5"
      case Value.Six => "" + color.toString.charAt(0) + " 6"
      case Value.Seven => "" + color.toString.charAt(0) + " 7"
      case Value.Eight => "" + color.toString.charAt(0) + " 8"
      case Value.Nine => "" + color.toString.charAt(0) + " 9"
      case Value.Suspend => "" + color.toString.charAt(0) + " S"
      case Value.DirectionChange => "" + color.toString.charAt(0) + " D"
      case Value.ColorChange => "" + color.toString.charAt(0) + " C"
      case Value.PlusTwo => "" + color.toString.charAt(0) + "+2"
      case Value.PlusFour => "" + color.toString.charAt(0) + "+4"
    }
  }

  def toGuiString:String = {
    value match {
      case Value.Zero => " 0 "
      case Value.One => " 1 "
      case Value.Two => " 2 "
      case Value.Three => " 3 "
      case Value.Four => " 4 "
      case Value.Five => " 5 "
      case Value.Six => " 6 "
      case Value.Seven => " 7 "
      case Value.Eight => " 8 "
      case Value.Nine => " 9 "
      case Value.Suspend => " S "//⊘
      case Value.DirectionChange => " D "//⇆
      case Value.ColorChange => " C "
      case Value.PlusTwo => "+ 2"
      case Value.PlusFour => "+ 4"
    }
  }
}
