package io.github.vigoo.repomorph.api

import java.io.File
import io.github.vigoo.repomorph.contexts.MorphContext
import io.github.vigoo.repomorph.{FilesInDirectory, SingleFile, FilePattern}

object MoveFluentAPI {
  def apply(from: String)(implicit context: MorphContext): MoveFluentAPI = new MoveFluentAPI(SingleFile(from))
  def files(implicit context: MorphContext): FilePatternFluentAPI[MoveFluentAPI, MoveFilesInDirectoryFluentAPI] = new FilePatternFluentAPI[MoveFluentAPI, MoveFilesInDirectoryFluentAPI](
    pattern => new MoveFluentAPI(pattern),
    filesInDir => new MoveFilesInDirectoryFluentAPI(filesInDir)
  )
}

class MoveFluentAPI(source: FilePattern)(implicit val context: MorphContext) {

  def to(target: String): Unit = {
    source match {
      case SingleFile(sourcePath) => context.move(new File(sourcePath), new File(target))
      case _ => ???
    }
  }
}

class MoveFilesInDirectoryFluentAPI(source: FilesInDirectory)(implicit val context: MorphContext) {
  def except(fileName: String): MoveFluentAPI = new MoveFluentAPI(source except fileName)
  def withExtension(extension: String): MoveFluentAPI = new MoveFluentAPI(source withExtension extension)
}