package io.github.vigoo.repomorph.api

import org.scalatest.FlatSpec
import org.scalamock.scalatest.MockFactory
import io.github.vigoo.repomorph.actions.FileAction
import io.github.vigoo.repomorph.contexts.MorphContext
import java.io.File

class ScriptAPISpec extends FlatSpec with MockFactory {

  import io.github.vigoo.repomorph.api.ScriptAPI._

  "ScriptAPI" should "provide a way to run actions on files" in {

    implicit val context = mock[MorphContext]
    val testAction = mock[FileAction]
    val pattern = files called "x"
    val x1 = new File("x")
    val x2 = new File("x")

    (context.getFiles _) expects(pattern) returns(List(x1, x2))
    (testAction.run (_: File) (_: MorphContext)).expects (x1, context).once
    (testAction.run (_: File) (_: MorphContext)).expects (x2, context).once

    foreach { pattern } { testAction }
  }

  "ScriptAPI" should "provide a way to move a single file to a directory" in {

    implicit val context = mock[MorphContext]

    (context.move _) expects(new File("source"), new File("target")) once()

    move { "source" } to { "target" }
  }

}
