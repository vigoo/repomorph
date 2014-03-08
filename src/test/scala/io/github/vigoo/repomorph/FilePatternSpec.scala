package io.github.vigoo.repomorph

import org.scalatest.FlatSpec
import org.scalatest.matchers.ShouldMatchers

class FilePatternSpec extends FlatSpec with ShouldMatchers {

  "FilesInDirectory" should "use its original filter when using `except`" in {

    val original = FilesInDirectory(".", _.startsWith("test"))
    val modified = original except "test2"

    modified match {
      case FilesInDirectory(_, f) => {
        f("a") should be (false)
        f("test1") should be (true)
        f("test2") should be (false)
        f("test3") should be (true)
      }
      case _ =>
    }
  }

  it should "use its original filter when using `withExtension`" in {
    val original = FilesInDirectory(".", _.startsWith("test"))
    val modified = original withExtension (".txt")

    modified match {
      case FilesInDirectory(_, f) => {
        f("a") should be (false)
        f("test") should be (false)
        f("test.txt") should be (true)
        f("test.scala") should be (false)
      }
      case _ =>
    }
  }
}
