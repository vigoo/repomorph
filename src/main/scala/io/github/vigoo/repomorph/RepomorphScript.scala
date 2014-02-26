package io.github.vigoo.repomorph

import resource._
import scala.io.Source
import java.io.{FileWriter, File}
import java.util.UUID
import com.googlecode.scalascriptengine.ScalaScriptEngine
import io.github.vigoo.repomorph.contexts.{MorphContext, FileSystemMorphContext}

class RepomorphScript(scriptSource: Source) {

  private val srcFolder = new File(System.getProperty("java.io.tmpdir"), UUID.randomUUID.toString)
  if (!srcFolder.mkdir) throw new IllegalStateException("can't create temp folder %s".format(srcFolder))
  private val srcFile = new File(srcFolder, "Script.scala")

  srcFile.deleteOnExit()
  srcFolder.deleteOnExit()

  private val source = s"""import io.github.vigoo.repomorph._
  import io.github.vigoo.repomorph.api._
  import io.github.vigoo.repomorph.contexts._
  import io.github.vigoo.repomorph.actions._
  import io.github.vigoo.repomorph.api.ScriptAPI._

  class UserScript extends Script {
    override def apply(context: MorphContext): Unit = {
      ${scriptSource.getLines().mkString("\n")}
    }
  }
  """

  private val config = ScalaScriptEngine.defaultConfig(srcFolder)
  private val sse = ScalaScriptEngine.onChangeRefresh(config, 0)

  for (src <- managed(new FileWriter(srcFile))) {
    src.write(source)
  }

  sse.refresh
  private val generatedClass = sse.get[Script]("UserScript")

  def run(context: MorphContext) {
    val instance = generatedClass.newInstance()
    instance(context)
  }
}
