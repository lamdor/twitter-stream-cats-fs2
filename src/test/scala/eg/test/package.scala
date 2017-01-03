package eg
package object test {
  val testConfig = Config(
    twitterOAuthConsumerKey = sys.env("TWITTER_OAUTH_CONSUMER_KEY"),
    twitterOAuthConsumerSecret = sys.env("TWITTER_OAUTH_CONSUMER_SECRET"),
    twitterOAuthToken = sys.env("TWITTER_OAUTH_TOKEN"),
    twitterOAuthTokenSecret = sys.env("TWITTER_OAUTH_TOKEN_SECRET"),
    twitterSampleStreamIncomingBufferSize = PositiveInt.valid(5).get,
    defaultExecutionContext = scala.concurrent.ExecutionContext.global,
    fs2Scheduler = fs2.Scheduler.fromFixedDaemonPool(2)
  )
}
