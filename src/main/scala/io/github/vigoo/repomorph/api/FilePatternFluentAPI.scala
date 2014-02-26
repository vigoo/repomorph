package io.github.vigoo.repomorph.api

import io.github.vigoo.repomorph._
import io.github.vigoo.repomorph.FilesInDirectory
import io.github.vigoo.repomorph.SingleFile
import io.github.vigoo.repomorph.FileByName

object FilePatternFluentAPI {
  def apply(name: String): FilePattern = SingleFile(name)
  def called(name: String): FilePattern = FileByName(name)
  def in(directoryName: String): FilesInDirectory = FilesInDirectory(directoryName, f => true)
  def withExtension(extension: String): FilePattern = FilesWithExtension(extension)
}
