package eg
package object arbInstances {

  import fs2.Stream
  import java.time.Instant
  import org.scalacheck.{Arbitrary, Gen}
  import Arbitrary.arbitrary

  implicit lazy val arbStatistics: Arbitrary[Statistics] = Arbitrary {
    for {
      count <- Gen.posNum[Int]
      firstCreatedAt <- arbitrary[Option[Instant]]
    } yield Statistics(count, firstCreatedAt)
  }

  implicit lazy val arbInstant: Arbitrary[Instant] = Arbitrary {
    Gen.posNum[Long].map(Instant.ofEpochMilli)
  }

  implicit def arbStream[A: Arbitrary]: Arbitrary[Stream[Nothing, A]] =
    Arbitrary {
      Gen.listOf(arbitrary[A]).map(as => Stream(as: _*))
    }

}
