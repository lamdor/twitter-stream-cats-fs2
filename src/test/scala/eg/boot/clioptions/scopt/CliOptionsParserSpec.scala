package eg.boot.clioptions.scopt

import eg.boot.clioptions.CliOptions
import org.specs2.mutable

object CliOptionsParserSpec extends mutable.Specification {
  "if defaults to env vars if no options given" >> {
    CliOptionsParser.parseArgsToOptions(Nil, sys.env).unsafeRun() must
      beSome.which(_ must not(beEmpty))
  }

  type ArgExpectation = Pair[String, (CliOptions => String)]
  val argExpectations = Seq.apply[ArgExpectation](
    ("twitter-oauth-consumer-key", (_.twitterOAuthConsumerKey)),
    ("twitter-oauth-consumer-secret", (_.twitterOAuthConsumerSecret)),
    ("twitter-oauth-token", (_.twitterOAuthToken)),
    ("twitter-oauth-token-secret", (_.twitterOAuthTokenSecret))
  )

  argExpectations.foreach {
    case (optName, getter) =>
      s"it sets the $optName if given" >> {
        val args = Seq(s"--$optName", "blah")
        CliOptionsParser.parseArgsToOptions(args, sys.env).unsafeRun() must
          beSome.which(opts => getter(opts) === "blah")
      }
  }

  "it uses all the supplied options" >> {
    val args = argExpectations.flatMap {
      case (k, _) => Seq(s"--${k}", "blah")
    }

    CliOptionsParser
      .parseArgsToOptions(args, Map.empty)
      .unsafeRun() must beSome.which { options =>
      argExpectations.forall { case (_, getter) => getter(options) === "blah" }
    }
  }

  "it returns none when it's missing options and env vars" >> {
    CliOptionsParser.parseArgsToOptions(Nil, Map.empty).unsafeRun() must beNone

    argExpectations.combinations(argExpectations.size - 1).foreach {
      expectations =>
        val args = expectations.flatMap {
          case (k, _) => Seq(s"--${k}", "blah")
        }

        CliOptionsParser
          .parseArgsToOptions(args, Map.empty)
          .unsafeRun() must beNone
    }
    ok
  }
}
