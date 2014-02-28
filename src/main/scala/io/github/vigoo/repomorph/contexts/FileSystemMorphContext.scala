package io.github.vigoo.repomorph.contexts

import resource._
import io.github.vigoo.repomorph._
import java.io.{FileInputStream, BufferedInputStream, FileWriter, File}
import org.apache.commons.io.FileUtils
import scala.io.Source
import io.github.vigoo.repomorph.FilesWithExtension
import io.github.vigoo.repomorph.SingleFile
import io.github.vigoo.repomorph.SingleDirectory
import java.util.UUID

class FileSystemMorphContext(private val rootPath: File) extends MorphContext {
  override def overwrite(file: File, newContents: String): Unit = {
    println(s"Overwriting ${file}")
    for (writer <- managed(new FileWriter(file))) {
      writer.write(newContents)
    }
  }

  def isUTF16(file: File): Boolean = {
    val stream = new FileInputStream(file)
    try {
      val bom = new Array[Byte](2)
      stream.read(bom, 0, 2)
      bom(0) == -1 && bom(1) == -2
    }
    finally {
      stream.close()
    }
  }

  override def read(file: File): Source =
    if (isUTF16(file))
      Source.fromFile(file, "UTF-16")
    else
      Source.fromFile(file, "UTF-8")

  override def getFiles(pattern: FilePattern): Iterable[File] =
    pattern match {
      case SingleFile(path) => List(new File(rootPath, path))
      case SingleDirectory(path) => safe(new File(rootPath, path).listFiles)
      case FilesWithExtension(extension) => for (file <- allFiles() if file.getName endsWith extension) yield file
      case FilesInDirectory(path, filter) => for (file <- safe(new File(rootPath, path).listFiles) if file.isFile && filter(file.getName)) yield file
      case FileByName(name) => for (file <- allFiles() if file.getName == name) yield file
    }

  override def move(source: File, target: File): Unit = {

    val absSource = new File(rootPath, source.getPath)
    val absTarget = new File(rootPath, target.getPath)

    println(s"Moving ${absSource} to ${absTarget}")

    if (absSource.isDirectory) {

      if (absTarget.getCanonicalPath.startsWith(absSource.getCanonicalPath))
      {
        val tempDir = new File(FileUtils.getTempDirectory, UUID.randomUUID().toString)
        FileUtils.moveDirectory(absSource, tempDir)
        FileUtils.moveDirectory(tempDir, absTarget)
      } else {
        FileUtils.moveDirectory(absSource, absTarget)
      }
    }
    else {
      absSource.renameTo(absTarget)
    }
  }

  override def delete(file: File): Unit = {
    println(s"Deleting ${file}")
    file.delete()
  }

  private def allFiles(root: File): Iterable[File] = {
    safe(root.listFiles).filter(_.isFile) ++ safe(root.listFiles).filter(_.isDirectory).flatMap(allFiles)
  }

  private def allFiles(): Iterable[File] = allFiles(rootPath)

  private def safe(s: Iterable[File]): Iterable[File] = if (s == null) List() else s

}
