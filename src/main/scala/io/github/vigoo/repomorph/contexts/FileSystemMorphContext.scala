package io.github.vigoo.repomorph.contexts

import resource._
import io.github.vigoo.repomorph.FilePattern
import java.io.{FileWriter, File}
import scala.io.Source

class FileSystemMorphContext extends MorphContext {
  override def overwrite(file: File, newContents: String): Unit = {
    for (writer <- managed(new FileWriter(file))) {
      writer.write(newContents)
    }
  }

  override def read(file: File): Source =
    Source.fromFile(file)

  override def getFiles(pattern: FilePattern): Iterable[File] = ???

  override def move(source: File, target: File): Unit =
    source.renameTo(target)

  override def delete(file: File): Unit =
    file.delete()
}
