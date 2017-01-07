package eg.boot.clioptions

import fs2.Task

trait CliOptionsParser[CliOptions] {
  def parseArgsToOptions(args: Seq[String],
                         env: Map[String, String]): Task[Option[CliOptions]]
}
