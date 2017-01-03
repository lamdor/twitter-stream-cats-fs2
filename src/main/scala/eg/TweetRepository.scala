package eg

import fs2.Stream

trait TweetRepository[Tweet, F[_]] {
  def tweetSampleStream: Configured[Stream[F, Tweet]] // TOOD: how about a stream of chunks?
}
