package eg.boot

import eg._
import eg.boot.clioptions._
import fs2.Task

object Main {
  object Program extends eg.Program[Tweet, Statistics, fs2.Task]

  def main(args: Array[String]): Unit = {
    scopt.CliOptionsParser
      .parseArgsToOptions(args, sys.env)
      .flatMap {
        case Some(cliOptions) =>
          val config =
            Config(
              twitterOAuthConsumerKey = cliOptions.twitterOAuthConsumerKey,
              twitterOAuthConsumerSecret =
                cliOptions.twitterOAuthConsumerSecret,
              twitterOAuthToken = cliOptions.twitterOAuthToken,
              twitterOAuthTokenSecret = cliOptions.twitterOAuthTokenSecret,
              twitterSampleStreamIncomingBufferSize = PositiveInt.valid(5).get,
              defaultExecutionContext =
                scala.concurrent.ExecutionContext.global,
              fs2Scheduler = fs2.Scheduler.fromFixedDaemonPool(2)
            )
          implicit val S = config.fs2DefaultStrategy

          Program(eg.dispatch.TweetRepository).run(config)

        case None => Task.now(())
      }
      .unsafeRun
  }
}
