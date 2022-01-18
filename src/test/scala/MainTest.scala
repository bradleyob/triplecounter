import org.scalatest.FunSuite
import org.scalatest.BeforeAndAfter

import scala.collection.mutable

class MainTest extends FunSuite with BeforeAndAfter  {
  before {
    Main.remainder = Seq()
    Main.cMap = mutable.Map()
  }

  test("Test the word counter"){
  assert(
    {
      Main.wordCounter("hello, dolly. How are you")
      Main.cMap("are, how, you") == 1
    }
  )
  }

  test("Test that contractions are normalized"){
    assert(
      {
        Main.wordCounter("hello, dolly. Ho'w are you")
        Main.cMap("are, how, you") == 1
      }
    )
  }

  test("Test that unicode is skipped"){
    assert(
      {
        Main.wordCounter("\uD83D\uDE0Ahello, dolly. Ho'w are you")
        Main.cMap("dolly, hello, how") == 1
      }
    )
  }

  test("Test that remainders are considered"){
    assert(
      {
        val lines = Seq(
         "to you", "500 years from now", "a message"
        )
        lines.foreach { line =>
          Main.streamWordCounter(line)
        }
        Main.cMap("500, to, you") == 1 &&
        Main.cMap("a, message, now") == 1
      }
    )
  }

  test("Test incrementation works and we maintain sorted order for all permutations"){
    assert(
      {
        val lines = Seq(
          "a b c",
          "a b c"
        )
        lines.foreach { line =>
          Main.streamWordCounter(line)
        }
        println(Main.cMap.toSeq.mkString("\n"))
        Main.cMap("a, b, c") == 4
      }
    )
  }
}
