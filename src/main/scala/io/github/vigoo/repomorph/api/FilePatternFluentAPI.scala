package io.github.vigoo.repomorph.api

import io.github.vigoo.repomorph._
import io.github.vigoo.repomorph.FilesInDirectory
import io.github.vigoo.repomorph.SingleFile
import io.github.vigoo.repomorph.FileByName

object FilePatternFluentAPI extends FilePatternFluentAPI[FilePattern, FilesInDirectory](identity, identity)

class FilePatternFluentAPI[TGeneric, TFilesInDir](
    private val genericTr: FilePattern=>TGeneric,
    private val filesInDirTr: FilesInDirectory=>TFilesInDir) {
  def apply(name: String): TGeneric = genericTr(SingleFile(name))
  def apply(names: String*): DirectorySpecificationAPI = new DirectorySpecificationAPI(names :_*)
  def called(name: String): TGeneric = genericTr(FileByName(name))
  def in(directoryName: String): TFilesInDir = filesInDirTr(FilesInDirectory(directoryName, f => true))
  def withExtension(extension: String): TGeneric = genericTr(FilesWithExtension(extension))

  def not(prj: FilesInProject): FilePattern = FilesInProject(prj.projectPath, !prj.inverted)
}
