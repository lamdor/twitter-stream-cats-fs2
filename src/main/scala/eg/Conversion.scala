package eg

trait Conversion[A, B] extends Function1[A, B] { // TODO: use bijection?
  def apply(a: A): B
}
