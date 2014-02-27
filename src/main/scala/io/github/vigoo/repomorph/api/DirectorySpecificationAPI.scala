package io.github.vigoo.repomorph.api

import io.github.vigoo.repomorph.FilesInDirectory
import scala.collection.immutable.Set

class DirectorySpecificationAPI(private val names: String*) {
  private val nameSet = Set(names : _*)

  def in(directory: String) = FilesInDirectory(directory, f => nameSet contains f)
  def from(directory: String) = in(directory)
}
