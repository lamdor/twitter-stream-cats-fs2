package eg

import cats.kernel.Monoid
import fs2.util.Async
import eg.fs2utils._
import scala.concurrent.duration._

trait Program[Tweet, Statistics, F[_]] {
  def program(config: Config, tweetRepo: TweetRepository[Tweet, F])(
      implicit TweetToStatsConversion: Conversion[Tweet, Statistics],
      M: Monoid[Statistics],
      A: Async[F]): F[Unit] = { // TODO: this should be F[Nothing]

    implicit val scheduler = config.fs2Scheduler

    val sampleStream = tweetRepo.tweetSampleStream.run(config)
    val stats = sampleStream.scanMapCombine(TweetToStatsConversion)
    val lastStats = fs2.async.hold(M.empty, stats)

    val every5Seconds = fs2.time.awakeEvery(5 seconds)

    lastStats
      .flatMap { statSignal =>
        statSignal.discrete
          .zip(every5Seconds)
          .map(println)
      }
      .drain
      .run
  }
}

object Program extends Program[Tweet, Statistics, fs2.Task]
