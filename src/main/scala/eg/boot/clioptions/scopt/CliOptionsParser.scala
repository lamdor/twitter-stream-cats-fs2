package eg.boot.clioptions.scopt

import eg.boot.clioptions
import eg.boot.clioptions.CliOptions
import fs2.Task
import scopt.OptionParser
import cats.syntax.flatMap._
import cats.instances.option._

trait CliOptionsParser extends clioptions.CliOptionsParser[CliOptions] {
  val parser = new OptionParser[CliOptions]("twitter-stream-cats-fs2") {
    opt[String]("twitter-oauth-consumer-key").action { (k, c) =>
      c.copy(twitterOAuthConsumerKey = k)
    }
    opt[String]("twitter-oauth-consumer-secret").action { (k, c) =>
      c.copy(twitterOAuthConsumerSecret = k)
    }
    opt[String]("twitter-oauth-token").action { (k, c) =>
      c.copy(twitterOAuthToken = k)
    }
    opt[String]("twitter-oauth-token-secret").action { (k, c) =>
      c.copy(twitterOAuthTokenSecret = k)
    }
  }

  def parseArgsToOptions(args: Seq[String],
                         env: Map[String, String]): Task[Option[CliOptions]] =
    Task.delay {
      parser.parse(args, CliOptions.empty).flatMap { options =>
        val withDefaults =
          defaultToEnvVarIfEmpty(options,
                                 env,
                                 options.twitterOAuthConsumerKey,
                                 "TWITTER_OAUTH_CONSUMER_KEY") { (c, s) =>
            c.copy(twitterOAuthConsumerKey = s)
          } >>= { o =>
            defaultToEnvVarIfEmpty(o,
                                   env,
                                   o.twitterOAuthConsumerSecret,
                                   "TWITTER_OAUTH_CONSUMER_SECRET") { (c, s) =>
              c.copy(twitterOAuthConsumerSecret = s)
            }
          } >>= { o =>
            defaultToEnvVarIfEmpty(o,
                                   env,
                                   o.twitterOAuthToken,
                                   "TWITTER_OAUTH_TOKEN") { (c, s) =>
              c.copy(twitterOAuthToken = s)
            }
          } >>= { o =>
            defaultToEnvVarIfEmpty(o,
                                   env,
                                   o.twitterOAuthTokenSecret,
                                   "TWITTER_OAUTH_TOKEN_SECRET") { (c, s) =>
              c.copy(twitterOAuthTokenSecret = s)
            }
          }

        if (withDefaults.isEmpty) {
          println("Missing twitter config values!")
        }

        withDefaults
      }
    }

  // TODO: this could be cleaner with lenses
  def defaultToEnvVarIfEmpty(options: CliOptions,
                             env: Map[String, String],
                             currentValue: String,
                             envVarName: String)(
      mod: (CliOptions, String) => CliOptions): Option[CliOptions] =
    if (currentValue.isEmpty()) {
      env.get(envVarName).map(mod(options, _))
    } else {
      Some(options)
    }
}

object CliOptionsParser extends CliOptionsParser
