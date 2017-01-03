package eg.dispatch

import com.ning.http.client.oauth.{ConsumerKey, RequestToken}
import dispatch.{Http, Req}
import dispatch.as.repatch.{twitter => asTwitter}
import dispatch.url
import eg._
import fs2.{Strategy, Stream, Task}
import org.json4s.JValue
import repatch.twitter.request._
import repatch.twitter.response.{Tweet => TTweet}

trait TweetRepository extends eg.TweetRepository[Tweet, Task] {
  type TTweetOrJson = Either[JValue, TTweet]

  lazy val tweetSampleStream: Configured[Stream[Task, Tweet]] =
    Configured { config =>
      implicit val ec = config.defaultExecutionContext
      implicit val S = config.fs2DefaultStrategy

      val oauthSign = mkOAuthClient(config)

      def httpStatusSample(http: Http)(f: TTweetOrJson => Unit): Task[Unit] =
        Task.delay {
          http(
            oauthSign(StatusesSample()) > asTwitter.response.stream
              .TweetOrJson(f))
        }

      val tweetOrJsons: Stream[Task, TTweetOrJson] =
        Stream.bracket[Task, Http, TTweetOrJson](
          Task.delay(Http())
        )(
          use = { http =>
            val bufferSize = config.twitterSampleStreamIncomingBufferSize.value
            val fq = for {
              queue <- fs2.async
                .unboundedQueue[Task, TTweetOrJson] // (bufferSize) // TOOD: why doesn't a bounded queue work!
              _ <- httpStatusSample(http)(unsafeSubscribeQueue(queue))
            } yield queue

            Stream.eval(fq).flatMap(_.dequeue)
          },
          release = { http =>
            Task.delay(http.shutdown())
          }
        )

      tweetOrJsons.collect {
        case Right(ttweet) =>
          Tweet(text = ttweet.text, createdAt = ttweet.created_at.toInstant())
      }
    }

  private def mkOAuthClient(config: Config): OAuthClient = {
    val consumerKey = new ConsumerKey(config.twitterOAuthConsumerKey,
                                      config.twitterOAuthConsumerSecret)
    val accessToken = new RequestToken(config.twitterOAuthToken,
                                       config.twitterOAuthTokenSecret)
    OAuthClient(consumerKey, accessToken)
  }

  // a way to hide the ugliness a bit
  private def unsafeSubscribeQueue[A](
      queue: fs2.async.mutable.Queue[Task, A]): (A => Unit) = { a =>
    queue.enqueue1(a).unsafeRun()
  }

  private case class StatusesSample(params: Map[String, String] = Map.empty)
      extends Method
      with Param[StatusesSample]
      with StreamParam[StatusesSample] {

    def complete =
      (req: Req) =>
        url("https://stream.twitter.com/1.1/statuses/sample.json") << params

    def param[A: Show](key: String)(value: A) =
      copy(params = params + (key -> implicitly[Show[A]].shows(value)))
  }

}

object TweetRepository extends TweetRepository
