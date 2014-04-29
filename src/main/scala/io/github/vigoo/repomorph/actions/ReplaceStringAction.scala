package io.github.vigoo.repomorph.actions

import java.io.File
import io.github.vigoo.repomorph.contexts.MorphContext
import resource._

class ReplaceStringAction(in: String, out: String) extends FileAction {
  override def run(file: File)(implicit context: MorphContext): Unit = {
    for (source <- managed(context.read(file))) {

      val newContents = scala.collection.mutable.ListBuffer[String]()
      var anyMatch = false

      for (line <- source.getLines()) {
        if (line.contains(in)) {
          anyMatch = true
          newContents += line.replaceAll(in, out)
        } else {
          newContents += line
        }
      }

      if (anyMatch) {
        println(s"Overwriting ${file.getName} with replaced strings...")
        context.overwrite(file, newContents.mkString("\n"))
      }
    }
  }
}
