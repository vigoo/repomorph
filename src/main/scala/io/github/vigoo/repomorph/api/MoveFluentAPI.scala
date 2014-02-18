package io.github.vigoo.repomorph.api

import java.io.File
import io.github.vigoo.repomorph.contexts.MorphContext
import io.github.vigoo.repomorph.{SingleFile, FilePattern}

class MoveFluentAPI(source: FilePattern)(implicit val context: MorphContext) {

  def to(target: String): Unit = {
    source match {
      case SingleFile(sourcePath) => context.move(new File(sourcePath), new File(target))
      case _ => ???
    }
  }
}
