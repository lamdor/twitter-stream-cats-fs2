package eg.fs2utils

import fs2.Stream
import org.specs2.{ScalaCheck, mutable}
import cats.instances.all._

object StreamOpsSpec extends mutable.Specification with ScalaCheck {
  "scanMapCombine" >> {
    "should have a length of the original plus one (for the zero)" >>
      prop { ss: List[Int] =>
        val s = Stream.apply(ss: _*)
        val Right(sums) = s.scanMapCombine(identity).runLog
        sums.length must beEqualTo(ss.length + 1)
      }

    "should be combining over the previous element " >> prop {
      (s1: Int, s2: Int, s3: Int) =>
        val s = Stream.apply(s1, s2, s3)
        val Right(sums) = s.scanMapCombine(identity).runLog
        sums must contain(exactly(0, s1, s1 + s2, s1 + s2 + s3))
    }

    "last should be total sum" >> prop { ss: List[String] =>
      val s = Stream.apply(ss: _*)
      val Right(Some(sum)) = s.scanMapCombine(_.length).lastOr(0).runLast
      sum must beEqualTo(ss.map(_.length).sum)
    }
  }
}
