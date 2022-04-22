package model.gameComponent.gameBaseImpl

case class Card(color: Color, value: Value):
  override def toString: String = 
    value match
      case Value.Suspend | Value.DirectionChange | Value.ColorChange => color.toString.charAt(0) + " " + value.toString.charAt(0)
      case Value.PlusTwo => color.toString.charAt(0) + "+2"
      case Value.PlusFour => color.toString.charAt(0) + "+4"
      case _ => color.toString.charAt(0) + " " + value.ordinal

  def toGuiString: String =
    value match
      case Value.Suspend | Value.DirectionChange | Value.ColorChange => " " + value.toString.charAt(0) + " "
      case Value.PlusTwo => "+ 2"
      case Value.PlusFour => "+ 4"
      case _ => " " + value.ordinal + " "