package io.github.vigoo.repomorph.actions

import java.io.File
import io.github.vigoo.repomorph.contexts.MorphContext

abstract class FileAction {
  def run(file: File)(implicit context: MorphContext): Unit
}
