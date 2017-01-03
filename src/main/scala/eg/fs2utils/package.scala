package eg

import fs2.Stream

package object fs2utils {

  import cats.kernel.Monoid

  implicit class StreamOps[F[_], A](stream: Stream[F, A]) {
    def scanMapCombine[B](f: A => B)(implicit M: Monoid[B]): Stream[F, B] =
      stream.scan(M.empty)((b, a) => M.combine(b, f(a)))
  }
}
