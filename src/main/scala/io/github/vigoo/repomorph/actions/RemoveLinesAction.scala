package io.github.vigoo.repomorph.actions

import java.io.File
import io.github.vigoo.repomorph.contexts.MorphContext
import resource._

class RemoveLinesAction(lineFilter: (String => Boolean)) extends FileAction {

  override def run(file: File)(implicit context: MorphContext): Unit = {
    for (source <- managed(context.read(file))) {
      val newContents = source.getLines().filterNot(lineFilter).mkString("\n")
      context.overwrite(file, newContents)
    }
  }
}
