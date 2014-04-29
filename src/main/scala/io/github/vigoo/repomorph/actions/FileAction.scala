package io.github.vigoo.repomorph.actions

import java.io.File
import io.github.vigoo.repomorph.contexts.MorphContext

abstract class FileAction {

  def run(file: File)(implicit context: MorphContext): Unit

  def and(other: FileAction): FileAction = {
    val self = this
    new FileAction {
      override def run(file: File)(implicit context: MorphContext): Unit = {
        self.run(file)
        other.run(file)
      }
    }
  }
}
