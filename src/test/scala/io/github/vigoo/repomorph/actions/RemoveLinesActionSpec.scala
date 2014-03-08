package io.github.vigoo.repomorph.actions

import org.scalatest.FlatSpec
import io.github.vigoo.repomorph.contexts.MorphContext
import org.scalamock.scalatest.MockFactory
import java.io.File
import scala.io.Source

class RemoveLinesActionSpec extends FlatSpec with MockFactory {

  "RemoveLinesAction" should "read and overwrite the given file using the morph context" in {

    implicit val context = mock[MorphContext]
    val testFile = new File("x")

    (context.read _).expects(testFile).once.returning(Source.fromString("test file"))
    (context.overwrite _).expects(testFile ,*).once

    val action = new RemoveLinesAction(_ => false)
    action.run(testFile)
  }

  it should "not touch the input lines the filter function skips" in {
    implicit val context = mock[MorphContext]
    val testFile = new File("x")

    (context.read _).expects(testFile).once.returning(Source.fromString("line1\nline2\nline3"))
    (context.overwrite _).expects(testFile, "line1\nline2\nline3").once

    val action = new RemoveLinesAction(_ => false)
    action.run(testFile)
  }

  it should "remove lines based on the given filter function" in {
    implicit val context = mock[MorphContext]
    val testFile = new File("x")

    (context.read _).expects(testFile).once.returning(Source.fromString("line1\nline2\nline3"))
    (context.overwrite _).expects(testFile, "line1\nline3").once

    val action = new RemoveLinesAction(_ == "line2")
    action.run(testFile)
  }

  it should "call the filter function for each line" in {
    implicit val context = mock[MorphContext]
    val testFile = new File("x")

    (context.read _).expects(testFile).once.returning(Source.fromString("line1\nline2\nline3"))
    (context.overwrite _).expects(testFile, *).once

    val filter = mockFunction[String, Boolean]
    filter.expects("line1").returning(false).once
    filter.expects("line2").returning(false).once
    filter.expects("line3").returning(false).once

    val action = new RemoveLinesAction(filter)
    action.run(testFile)
  }
}
