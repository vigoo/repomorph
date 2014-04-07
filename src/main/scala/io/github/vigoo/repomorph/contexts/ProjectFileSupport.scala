package io.github.vigoo.repomorph.contexts

import java.io.File

trait ProjectFileSupport extends VisualStudioProjectSupport {
  self: MorphContext =>

  def filesInProject(projectPath: File, inverted: Boolean): Set[File] = {
    if (projectPath.getName.toLowerCase.endsWith(".sln")) {
      filesInVisualStudioSolution(projectPath, inverted)
    }
    else if (projectPath.getName.toLowerCase.endsWith(".csproj")) {
      filesInVisualCSharpProject(projectPath, inverted)
    }
    else {
      Set()
    }
  }
}
