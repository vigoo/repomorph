package io.github.vigoo.repomorph.contexts

import java.io.File
import resource._
import io.github.vigoo.repomorph.FilesInDirectory

trait VisualStudioProjectSupport {

  self: MorphContext =>

  def filesInVisualStudioSolution(solution: File, inverted: Boolean): Set[File] = {

    val pattern = "^Project\\(\\\".+\\\"\\)\\s=\\s\\\"([^\"]+\\\\.csproj)\\\",".r

    managed(self.read(solution)) acquireAndGet {
      source =>

      val projectFiles = source.getLines
        .map(line => pattern findFirstIn line)
        .collect {
          case Some(pattern(csprojPath)) => new File(solution.getParent, csprojPath) }
        .toSet

      for (projectFile <- projectFiles;
           sourceFile <- filesInVisualCSharpProject(projectFile, inverted))
        yield sourceFile
    }
  }

  def filesInVisualCSharpProject(projectPath: File, inverted: Boolean): Set[File] = {

    val pattern = "\\<Compile\\sInclude=\\\"([^\"]+)\\\"\\s\\/\\>".r

    managed(self.read(projectPath)) acquireAndGet {
      source =>
        val sourceFiles = source.getLines
          .map(line => pattern findFirstIn line)
          .collect {
             case Some(pattern(filePath)) => new File(projectPath.getParent, filePath) }
          .toSet

        if (inverted) {
          getFilesWithExtension(projectPath.getParentFile, ".cs").filter(f => !sourceFiles.contains(f))
        } else {
          sourceFiles
        }
    }
  }

  private def getFilesWithExtension(root: File, extension: String): Set[File] = {
      allFiles(root).filter(f => f.getName.endsWith(extension)).toSet
  }
}