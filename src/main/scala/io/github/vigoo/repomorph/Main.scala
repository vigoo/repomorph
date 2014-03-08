package io.github.vigoo.repomorph

import java.io.File
import scala.io.Source
import scopt.OptionParser
import io.github.vigoo.repomorph.contexts.{MercurialMorphContext, MorphContext, FileSystemMorphContext}

case class RepomorphConfig(script: File = new File("script.morph"), root: File = new File("."))

object Main {

  private def createContext(root: File): MorphContext = {
    val hgdir = new File(root, ".hg")
    if (hgdir.exists() && hgdir.isDirectory) {
      new MercurialMorphContext(root)
    } else {
      new FileSystemMorphContext(root)
    }
  }

  def main(args: Array[String]) {
    val parser = new OptionParser[RepomorphConfig]("repomorph") {
      head("repomorph", "1.0")
      arg[File]("<script>") required() action { (x, c) => c.copy(script = x) } text("the repomorph script to be executed")
      arg[File]("<root>") required() action { (x, c) => c.copy(root = x) } text("repository root")
      help("help") text("prints this help")
    }

    parser.parse(args, RepomorphConfig()) map { config =>
      val script = new RepomorphScript(Source.fromFile(config.script))
      script.run(createContext(config.root))
    }
  }
}
