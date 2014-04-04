package io.github.vigoo.repomorph

abstract class FilePattern

case class SingleFile(path: String) extends FilePattern {}
case class SingleDirectory(path: String) extends FilePattern
case class FilesWithExtension(extension: String) extends FilePattern
case class FilesInDirectory(path: String, filter: String=>Boolean) extends FilePattern {
  def except(fileName: String) = FilesInDirectory(path, f => filter(f) && f != fileName)
  def withExtension(extension: String) = FilesInDirectory(path, f => filter(f) && f.endsWith(extension))
}
case class FileByName(name: String) extends  FilePattern
case class FilesInProject(projectPath: String, inverted: Boolean) extends FilePattern
