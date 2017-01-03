package eg

class PositiveInt private (val value: Int) extends AnyVal

object PositiveInt {
  def valid(i: Int): Option[PositiveInt] =
    if (i > 0) {
      Some(new PositiveInt(i))
    } else {
      None
    }
}
