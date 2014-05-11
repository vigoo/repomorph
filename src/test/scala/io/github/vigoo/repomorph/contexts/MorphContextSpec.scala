package io.github.vigoo.repomorph.contexts

import scala.collection.immutable.List
import org.scalatest._
import org.scalatest.Matchers._
import java.nio.file.Files
import org.apache.commons.io.FileUtils
import com.aragost.javahg.Repository
import io.github.vigoo.repomorph.{WholeRepository, FilesInDirectory}
import java.io.File
import com.aragost.javahg.commands.{CommitCommand, AddCommand}
import org.scalatest.matchers.Matcher

abstract class MorphContextSpec[TContext <: MorphContext](implName: String) extends FlatSpec {

  def haveFileName(name: String): Matcher[File] =
     be(name) compose { (f: File) => f.getName }

  def havePathEndingWith(postfix: String): Matcher[File] =
    endWith(postfix) compose { (f: File) => f.getAbsolutePath }

  implName should "get the list of all files" in {

    withTestContext { implicit context =>
      files("a", "b", "c")

      val returnedFiles = context.getFiles(FilesInDirectory("", _ => true))

      exactly (1, returnedFiles) should haveFileName("a")
      exactly (1, returnedFiles) should haveFileName("b")
      exactly (1, returnedFiles) should haveFileName("c")
    }
  }

  it should "be able to delete non-empty directories recursively" in {
    withTestContext { implicit context =>

      directory("d1")
      directory("d1/d3")
      directory("d2")
      files("d1/a", "d1/d3/b")

      context.delete("d1")

      directoryExists("d1") should be(false)
      directoryExists("d2") should be(true)
    }
  }

  it should "be able to delete files one by one" in {
    withTestContext { implicit context =>

      files("a", "b", "c")

      context.delete("b")

      val returnedFiles = context.getFiles(FilesInDirectory("", _ => true))

      returnedFiles should have length(2)
      exactly(1, returnedFiles) should haveFileName("a")
      exactly(1, returnedFiles) should haveFileName("c")
    }
  }

  it should "be able to move directories" in {
    withTestContext { implicit context =>

      directory("d")
      directory("d/d2")
      files("d/a", "d/d2/b", "c")

      context.move(new File("d"), new File("target/inner"))

      val resultFiles = context.getFiles(WholeRepository)

      resultFiles should have length (3)
      exactly(1, resultFiles) should havePathEndingWith("target/inner/a")
      exactly(1, resultFiles) should havePathEndingWith("target/inner/d2/b")
      exactly(1, resultFiles) should havePathEndingWith("c")

      directoryExists("target") should be (true)
      directoryExists("target/inner") should be (true)
      directoryExists("target/inner/d2") should be (true)
      directoryExists("d") should be (false)
    }
  }

  def createTestContext: TContext
  def cleanup(context: TContext)

  def files(names: String*)(implicit context: TContext) = names.map(file)
  def file(name: String)(implicit context: TContext)
  def directory(name: String)(implicit context: TContext)
  def directoryExists(name: String)(implicit context: TContext): Boolean

  def withTestContext(test: TContext => Unit): Unit = {
    implicit val context = createTestContext
    try {
      test(context)
    }
    finally {
      cleanup(context)
    }
  }
}

class FileSystemMorphContextSpec extends MorphContextSpec[FileSystemMorphContext]("file system morph context") {
  override def createTestContext: FileSystemMorphContext = {

    val root = Files.createTempDirectory("fsmorphcontext-test").toFile
    val context = new FileSystemMorphContext(root)

    context
  }

  override def cleanup(context: FileSystemMorphContext): Unit = {
    FileUtils.deleteDirectory(context.rootPath)
  }

  override def file(name: String)(implicit context: FileSystemMorphContext): Unit = {
    val file = new File(context.rootPath, name)
    file.createNewFile()
  }

  override def directory(name: String)(implicit context: FileSystemMorphContext): Unit = {
    val dir = new File(context.rootPath, name)
    dir.mkdirs()
  }

  override def directoryExists(name: String)(implicit context: FileSystemMorphContext): Boolean = {
    val dir = new File(context.rootPath, name)
    dir.isDirectory && dir.exists
  }
}

class MercurialMorphContextSpec extends MorphContextSpec[MercurialMorphContext]("mercurial morph context") {
  override def createTestContext: MercurialMorphContext = {
    val root = Files.createTempDirectory("hgmorphcontext-test").toFile
    Repository.create(root)

    val context = new MercurialMorphContext(root)

    context
  }

  override def cleanup(context: MercurialMorphContext): Unit = {
    FileUtils.deleteDirectory(context.rootPath)
  }

  override def file(name: String)(implicit context: MercurialMorphContext): Unit = {
    val file = new File(context.rootPath, name)
    file.createNewFile()

    val repo = Repository.open(context.rootPath)
    new AddCommand(repo).execute(file)

    commit(repo, s"Added $name")
  }

  override def directory(name: String)(implicit context: MercurialMorphContext): Unit = {
    val dir = new File(context.rootPath, name)
    dir.mkdirs()
  }

  override def directoryExists(name: String)(implicit context: MercurialMorphContext): Boolean = {
    val dir = new File(context.rootPath, name)
    dir.isDirectory && dir.exists
  }

  private def commit(repo: Repository, message: String): Unit = {
    new CommitCommand(repo).message(message).user("test").execute()
  }
}