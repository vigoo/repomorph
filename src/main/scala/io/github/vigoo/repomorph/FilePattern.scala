package io.github.vigoo.repomorph

abstract class FilePattern
case class SingleFile(path: String) extends  FilePattern
case class SingleDirectory(path: String) extends FilePattern
case class FilesWithExtension(extension: String) extends FilePattern
case class FilesInDirectory(path: String, exceptions: Set[String]) extends FilePattern {
  def except(fileName: String) = FilesInDirectory(path, Set(fileName))
}
case class FileByName(name: String) extends  FilePattern
