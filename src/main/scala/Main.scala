import scala.collection.mutable
import scala.io.{BufferedSource, StdIn}

object Main extends App {
  if ((args.contains("-h") || args.contains("--help"))) {
    println(
      """Triple Counter
        | ------------
        | Provide a list of files to count the recurring triples
        | in each file: java -jar triplecounter.jar <file1> ... <fileN>
        | The counter also accepts streamed input from STDIN : cat "some text here" | java -jar triplecounter.jar
        |  """.stripMargin)
    sys.exit()
  }

  // Would rather not have some object state, but is most straightforward
  var cMap: mutable.Map[String, Int] = mutable.Map()
  var remainder: Seq[String] = Seq()

  if (!args.isEmpty) {
    println(processFiles(args)(fileWordCounter).map(printResult))
    sys.exit()
  }

  var line = ""
  while ({line = StdIn.readLine(); line != null})  {
    processStream(line)(streamWordCounter)
  }
  println(printResult(cMap.toMap))

  // Take top 100 and make more readable
  def printResult(cMap: Map[String, Int]) = {
    cMap.toSeq.sortBy{case (triple, count) => count}.reverse.take(100).mkString("\n")
  }

  def processStream(line: String)(processor: (String) => Unit): Unit = {
    processor(line)
  }

  def processFiles(fileNames: Array[String])(processor: BufferedSource => Map[String, Int]): Seq[Map[String, Int]] = {
    fileNames.map { fileName =>
      val file = io.Source.fromFile(fileName)
      processor(file)
    }.toSeq
  }

  def getWordsFromLine(line: String): Seq[String] =
    line
      // Normalize to lowercase
      .toLowerCase
      // Normalize contractions
      .replaceAll("'", "")
      // Replace unicode symbols with a space
      .replaceAll("[^\\u0000-\\u007F]", " ")
      // Replace brackets with spaces
      .replaceAll("[a-z0-9-]", " ")
      // Remove any spaces as a result of leading unicode char
      .trim
      // split on punctuation and whitespace
      .split("[ .,?!\\\";:]+")

  def updateTriples(cMap: mutable.Map[String, Int], triple: Seq[String]): Unit = {
    cMap.updateWith(triple.sorted.mkString(", ")) {
      case None => Some(1)
      case Some(numSeen) => Some(numSeen + 1)
    }
  }

  def processRemainders(remainder: Seq[String], line: Seq[String]): Seq[String] = {
    remainder match {
      case Seq() => Seq()
      case words@_ => {
        line match {
          case Seq() => Seq()
          case Seq(word) => {
            // We're probably done, persist this single word to the stack anyway to be sure
            remainder.appended(word)
          }
          case Seq(a, b, _*) => {
            if (words.length > 1)
              updateTriples(cMap, Seq(words(0), words(1), a))
            updateTriples(cMap, Seq(words.last, a, b))
            Seq()
          }
        }
      }
    }
  }

  def fileWordCounter(file: BufferedSource): Map[String, Int] = {
    file.getLines().foreach { line =>
      wordCounter(line)
    }
    cMap.toMap
  }

  def streamWordCounter(line: String) =
    wordCounter(line)

  def wordCounter(line: String): Unit = {
    val splitLine = getWordsFromLine(line)
    processRemainders(remainder, splitLine)
    splitLine.sliding(3).foreach {
      case Seq() => () // Empty Line
      case triple@Seq(x, y, z) => {
        updateTriples(cMap, triple)
        // persist last words as remainder
        remainder = Seq(y, z)
      }
      case shortLine@_ => {
        remainder = shortLine
      }
    }
  }
}