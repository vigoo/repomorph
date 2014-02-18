package io.github.vigoo.repomorph.api

import io.github.vigoo.repomorph.{SingleDirectory, FilePattern}

object DirectoryPatternFluentAPI {
  def called(name: String): FilePattern = SingleDirectory(name)
}
