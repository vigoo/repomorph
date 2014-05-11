package io.github.vigoo.repomorph.contexts

import java.io.File
import com.aragost.javahg.{RepositoryConfiguration, Repository}
import java.nio.charset.CodingErrorAction
import com.aragost.javahg.commands.{RemoveCommand, RenameCommand}

class MercurialMorphContext(rootPath: File) extends FileSystemMorphContext(rootPath) {

  private val repositoryConfig = new RepositoryConfiguration {
    setCodingErrorAction(CodingErrorAction.REPLACE)
  }
  private val repository = Repository.open(repositoryConfig, rootPath)

  override def delete(path: String): Unit = {
    val absPath = new File(rootPath, path)
    delete(absPath)
  }

  override def delete(file: File): Unit = {

    try {
      val command = new RemoveCommand(repository).force()
      command.execute(file)

      if (!command.isSuccessful) {
        println(s"Delete returned with ${command.getReturnCode}; error message: ${command.getErrorString}")
      }
    }
    catch {
      case e: Throwable => println(s"Delete failed: $e")
    }
  }

  override def move(source: File, target: File): Unit = {
    val absSource = new File(rootPath, source.getPath)
    val absTarget = new File(rootPath, target.getPath)

    try {
      val command = new RenameCommand(repository).force()
      command.execute(absSource, absTarget)

      if (command.getReturnCode != 0) {
        println(s"Rename returned with ${command.getReturnCode}; error message: ${command.getErrorString}")
      }
    }
    catch {
      case e: Throwable => println(s"Rename failed: $e")
    }
  }

  override def includeDirectory(dir: File): Boolean = dir.getName != ".hg"
}
