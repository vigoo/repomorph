package io.github.vigoo.repomorph

import org.scalatest.FlatSpec
import org.scalatest.matchers.ShouldMatchers
import scala.io.Source
import org.scalamock.scalatest.MockFactory
import io.github.vigoo.repomorph.contexts.MorphContext


class RepomorphScriptSpec extends FlatSpec with MockFactory with ShouldMatchers {

  "RepomorphScript" should "accept empty scripts" in {

    val script = new RepomorphScript(Source.fromString(""))
    val context = mock[MorphContext]
    script.run(context)
  }

  it should "provide the morph context to the script" in {

    val script = new RepomorphScript(Source.fromString("context.delete(\"xyz\")\n"))
    val context = mock[MorphContext]
    (context.delete (_:String)) expects ("xyz") once

    script.run(context)
  }

}
