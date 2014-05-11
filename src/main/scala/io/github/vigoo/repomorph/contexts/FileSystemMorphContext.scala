package io.github.vigoo.repomorph.contexts

import resource._
import io.github.vigoo.repomorph._
import java.io.{FileInputStream, FileWriter, File}
import org.apache.commons.io.FileUtils
import scala.io.Source
import io.github.vigoo.repomorph.FilesWithExtension
import io.github.vigoo.repomorph.SingleFile
import io.github.vigoo.repomorph.SingleDirectory
import java.util.UUID

class FileSystemMorphContext(val rootPath: File) extends MorphContext with ProjectFileSupport {
  override def overwrite(file: File, newContents: String): Unit = {
    println(s"Overwriting $file")
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

  override def getFiles(pattern: FilePattern): Seq[File] =
    pattern match {
      case SingleFile(path) => List(new File(rootPath, path))
      case SingleDirectory(path) => safe(new File(rootPath, path).listFiles)
      case FilesWithExtension(extension) => for (file <- allFiles() if file.getName endsWith extension) yield file
      case FilesInDirectory(path, filter) => for (file <- safe(new File(rootPath, path).listFiles) if file.isFile && filter(file.getName)) yield file
      case FileByName(name) => for (file <- allFiles() if file.getName == name) yield file
      case FilesInProject(projectPath, inverted) => filesInProject(new File(rootPath, projectPath), inverted).toSeq
      case WholeRepository => allFiles()
    }

  override def move(source: File, target: File): Unit = {

    val absSource = if (source.isAbsolute) source else new File(rootPath, source.getPath)
    val absTarget = if (target.isAbsolute) target else new File(rootPath, target.getPath)

    println(s"Moving $absSource to $absTarget")

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
      if (absTarget.exists && absTarget.isDirectory) {
        absSource.renameTo(new File(absTarget, absSource.getName))
      } else {
        val dir = absTarget.getParentFile
        dir.mkdirs()
        absSource.renameTo(absTarget)
      }
    }
  }

  override def delete(file: File): Unit = {
    println(s"Deleting $file")
    file.delete()
  }

  override def delete(path: String): Unit = {
    val absPath = new File(rootPath, path)
    println(s"Deleting $absPath")

    if (absPath.isDirectory) {
      FileUtils.deleteDirectory(absPath)
    } else {
      absPath.delete()
    }
  }

  override def allFiles(root: File): Seq[File] = {
    safe(root.listFiles).filter(_.isFile).filter(includeFile) ++ safe(root.listFiles).filter(_.isDirectory).filter(includeDirectory).flatMap(allFiles)
  }

  def includeFile(file: File): Boolean = true
  def includeDirectory(dir: File): Boolean = true

  private def allFiles(): Seq[File] = allFiles(rootPath)

  private def safe(s: Seq[File]): Seq[File] = if (s == null) List() else s

}
