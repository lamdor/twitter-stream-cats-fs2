package eg

import scala.concurrent.ExecutionContext
import fs2.Scheduler

case class Config(
    twitterOAuthConsumerKey: String,
    twitterOAuthConsumerSecret: String,
    twitterOAuthToken: String,
    twitterOAuthTokenSecret: String,
    twitterSampleStreamIncomingBufferSize: PositiveInt,
    defaultExecutionContext: ExecutionContext,
    fs2Scheduler: Scheduler
) {
  lazy val fs2DefaultStrategy =
    fs2.Strategy.fromExecutionContext(defaultExecutionContext)
}
