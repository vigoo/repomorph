package io.github.vigoo.repomorph

import java.io.File
import scala.io.Source
import scopt.OptionParser
import com.googlecode.scalascriptengine.EvalCode
import io.github.vigoo.repomorph.contexts.FileSystemMorphContext

case class RepomorphConfig(script: File = new File("script.morph"))

object Main {

  def main(args: Array[String]) {
    val parser = new OptionParser[RepomorphConfig]("repomorph") {
      head("repomorph", "1.0")
      arg[File]("<script>") required() action { (x, c) => c.copy(script = x) } text("the repomorph script to be executed")
      help("help") text("prints this help")
    }

    parser.parse(args, RepomorphConfig()) map { config =>
      val script = new RepomorphScript(Source.fromFile(config.script))
      script.run(new FileSystemMorphContext)
    }
  }
}
