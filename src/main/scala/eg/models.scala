package eg

import java.time.Instant

import cats.Apply
import cats.instances.option._
import cats.kernel.{CommutativeMonoid, Eq}

case class Tweet(text: String, createdAt: Instant)

case class Statistics(
    count: Int,
    firstCreatedAt: Option[Instant]
)

object Statistics {

  implicit lazy val monoid: CommutativeMonoid[Statistics] =
    new CommutativeMonoid[Statistics] {
      def empty = Statistics(count = 0, firstCreatedAt = None)

      def combine(s1: Statistics, s2: Statistics): Statistics = {
        Statistics(
          count = s1.count + s2.count,
          firstCreatedAt =
            minInstantOption(s1.firstCreatedAt, s2.firstCreatedAt)
        )
      }

      private[this] def minInstantOption(
          i1: Option[Instant],
          i2: Option[Instant]): Option[Instant] =
        Apply.semigroup[Option, Instant].combine(i1, i2) orElse i1 orElse i2
    }

  implicit lazy val eq: Eq[Statistics] = Eq.fromUniversalEquals

  implicit lazy val tweetConversion: Conversion[Tweet, Statistics] =
    new Conversion[Tweet, Statistics] {
      def apply(tweet: Tweet): Statistics =
        Statistics(1, Some(tweet.createdAt))
    }

  implicit lazy val minInstant: CommutativeMonoid[Instant] =
    new CommutativeMonoid[Instant] {
      def empty = Instant.ofEpochMilli(0)
      def combine(i1: Instant, i2: Instant): Instant = {
        if (i1.isBefore(i2)) {
          i1
        } else {
          i2
        }
      }
    }
}
