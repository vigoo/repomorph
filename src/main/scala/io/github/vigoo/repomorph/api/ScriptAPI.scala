package io.github.vigoo.repomorph.api

import java.io.File
import io.github.vigoo.repomorph.{SingleDirectory, FilePattern}
import io.github.vigoo.repomorph.actions.{FileAction, RemoveLinesAction}
import io.github.vigoo.repomorph.contexts.MorphContext

object ScriptAPI {
  def foreach(pattern: FilePattern)(action: FileAction)(implicit context: MorphContext): Unit = {
    for (file <- context.getFiles(pattern)) {
      action.run(file)
    }
  }

  def move = MoveFluentAPI

  def delete(pattern: FilePattern)(implicit context: MorphContext): Unit = {

    pattern match {
      case SingleDirectory(dir) => context.delete(dir)
      case _ => for (file <- context.getFiles(pattern)) {
        context.delete(file)
      }
    }
  }

  def file = FilePatternFluentAPI
  def files = FilePatternFluentAPI
  def directory = DirectoryPatternFluentAPI

  lazy val showPath: FileAction = new FileAction {
    override def run(file: File)(implicit context: MorphContext): Unit = println(file.getAbsolutePath)
  }

  def removeLines(lineFilter: String => Boolean): FileAction = new RemoveLinesAction(lineFilter)
}
