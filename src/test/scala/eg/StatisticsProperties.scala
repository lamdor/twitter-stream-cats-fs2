package eg

import org.specs2.Specification
import org.typelevel.discipline.specs2.Discipline
import cats.kernel.laws.GroupLaws
import arbInstances._

class StatisticsProperties extends Specification with Discipline {

  def is =
    checkAll("Statistics", GroupLaws[Statistics].commutativeMonoid)

}
