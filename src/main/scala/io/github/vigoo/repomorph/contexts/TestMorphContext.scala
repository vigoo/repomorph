package io.github.vigoo.repomorph.contexts

import io.github.vigoo.repomorph.FilePattern
import java.io.File
import scala.io.Source

object TestMorphContext extends MorphContext {
  override def getFiles(pattern: FilePattern): Iterable[File] = {
    List[File]()
  }

  override def read(file: File): Source = {
    println(s"Reading file ${file.getAbsolutePath}")
    Source.fromString("test")
  }

  override def overwrite(file: File, newContents: String): Unit = {
    println(s"Overwriting file ${file.getAbsolutePath}")
  }

  override def move(source: File, target: File): Unit = {
    println(s"Moving ${source.getAbsolutePath} to ${target.getAbsolutePath}")
  }

  override def delete(file: File): Unit = {
    println(s"Deleting ${file.getAbsolutePath}")
  }

  override def delete(path: String): Unit = {
    println(s"Deleting ${path}")
  }

  override def allFiles(root: File): Iterable[File] = {
    List[File]()
  }
}
