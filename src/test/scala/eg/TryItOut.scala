package eg

import eg.test.testConfig

object TryItOut {
  def main(args: Array[String]): Unit = {
    implicit val S = testConfig.fs2DefaultStrategy
    implicit val Scheduler = testConfig.fs2Scheduler

    Program.program(testConfig, eg.dispatch.TweetRepository).unsafeRun()
  }
}
