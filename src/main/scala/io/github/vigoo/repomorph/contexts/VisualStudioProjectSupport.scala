package io.github.vigoo.repomorph.contexts

import java.io.File
import resource._

trait VisualStudioProjectSupport {

  self: MorphContext =>

  def filesInVisualStudioSolution(solution: File, inverted: Boolean): Set[File] = {

    val pattern = """^Project\(\".+\"\)\s=\s\"[^\"]+\",\s\"([^\"]+\.csproj)\",""".r

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

    val pattern = """<(Compile|Page|ApplicationDefinition)\sInclude=\"([^\"]+)\"""".r

    managed(self.read(projectPath)) acquireAndGet {
      source =>
        val sourceFiles = source.getLines
          .map(line => pattern findFirstIn line)
          .collect {
             case Some(pattern(_, filePath)) => new File(projectPath.getParent, filePath) }
          .toSet

        if (inverted) {
          getFilesWithExtension(projectPath.getParentFile, Set(".cs", ".xaml")).filter(f => !sourceFiles.contains(f))
        } else {
          sourceFiles
        }
    }
  }

  private def getFilesWithExtension(root: File, extensions: Set[String]): Set[File] = {
      allFiles(root).filter(
        f => extensions.exists(extension => f.getName.endsWith(extension))).toSet
  }
}
