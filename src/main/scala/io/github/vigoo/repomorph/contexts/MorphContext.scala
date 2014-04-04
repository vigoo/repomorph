package io.github.vigoo.repomorph.contexts

import java.io.File
import scala.io.Source
import io.github.vigoo.repomorph.FilePattern

trait MorphContext {
  def getFiles(pattern: FilePattern): Iterable[File]
  def read(file: File): Source
  def overwrite(file: File, newContents: String): Unit
  def move(source: File, target: File): Unit
  def delete(file: File): Unit
  def delete(path: String): Unit
  def allFiles(root: File): Iterable[File];
}
