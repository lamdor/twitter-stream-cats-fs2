package eg.dispatch

import eg.test._
import org.specs2.mutable

object TweetRepositorySpec extends mutable.Specification {
  tag("slow")
  "reads tweets off the sample stream" in {
    val stream = TweetRepository.tweetSampleStream.run(testConfig)

    val toTake = testConfig.twitterSampleStreamIncomingBufferSize
    val tweets = stream.take(toTake.value.toLong).runLog.unsafeRun()
    tweets must haveLength(toTake.value)
  }
}
