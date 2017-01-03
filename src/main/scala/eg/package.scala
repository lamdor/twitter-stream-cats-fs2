package object eg {

  import cats.data.Reader

  type Configured[A] = Reader[Config, A]
  def Configured[A](f: Config => A): Configured[A] = Reader(f)
}
