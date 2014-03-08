package io.github.vigoo.repomorph.contexts

import java.io.File
import com.aragost.javahg.{RepositoryConfiguration, Repository}
import java.nio.charset.CodingErrorAction
import com.aragost.javahg.commands.{RemoveCommand, RenameCommand}

class MercurialMorphContext(private val rootPath: File) extends FileSystemMorphContext(rootPath) {

  private val repositoryConfig = new RepositoryConfiguration {
    setCodingErrorAction(CodingErrorAction.REPLACE)
  }
  private val repository = Repository.open(repositoryConfig, rootPath)

  override def delete(path: String): Unit = {
    val absPath = new File(rootPath, path)
    delete(absPath)
  }

  override def delete(file: File): Unit = {
    val command = new RemoveCommand(repository).force()
    command.execute(file)

    if (!command.isSuccessful) {
      println(s"Delete returned with ${command.getReturnCode}; error message: ${command.getErrorString}")
    }
  }

  override def move(source: File, target: File): Unit = {
    val command = new RenameCommand(repository).force()
    command.execute(source, target)

    if (command.getReturnCode != 0) {
      println(s"Rename returned with ${command.getReturnCode}; error message: ${command.getErrorString}")
    }
  }
}
