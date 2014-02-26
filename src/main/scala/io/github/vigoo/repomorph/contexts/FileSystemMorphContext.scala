package io.github.vigoo.repomorph.contexts

import resource._
import io.github.vigoo.repomorph._
import java.io.{FileWriter, File}
import scala.io.Source
import io.github.vigoo.repomorph.FilesWithExtension
import io.github.vigoo.repomorph.SingleFile
import io.github.vigoo.repomorph.SingleDirectory

class FileSystemMorphContext(private val rootPath: File) extends MorphContext {
  override def overwrite(file: File, newContents: String): Unit = {
    for (writer <- managed(new FileWriter(file))) {
      writer.write(newContents)
    }
  }

  override def read(file: File): Source =
    Source.fromFile(file)

  override def getFiles(pattern: FilePattern): Iterable[File] =
    pattern match {
      case SingleFile(path) => List(new File(path))
      case SingleDirectory(path) => new File(path).listFiles
      case FilesWithExtension(extension) => for (file <- allFiles() if file.getName endsWith extension) yield file
      case FilesInDirectory(path, filter) => for (file <- new File(path).listFiles if file.isFile && filter(file.getName)) yield file
      case FileByName(name) => for (file <- allFiles() if file.getName == name) yield file
    }

  override def move(source: File, target: File): Unit =
    source.renameTo(target)

  override def delete(file: File): Unit =
    file.delete()

  private def allFiles(root: File): Iterable[File] = {
    root.listFiles.filter(_.isFile) ++ root.listFiles.filter(_.isDirectory).flatMap(allFiles)
  }

  private def allFiles(): Iterable[File] = allFiles(rootPath)
}
