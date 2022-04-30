package fileIoComponent.fileIoJsonImpl

import com.google.inject.{Guice, Key}
import com.google.inject.name.Names
import fileIoComponent.FileIOInterface
import play.api.libs.json.*

import scala.annotation.tailrec
import scala.collection.mutable.ListBuffer
import scala.io.Source
import scala.util.Try

class FileIO extends FileIOInterface:
  override def load(): Try[String] = Try { Source.fromFile("game.json").getLines().mkString }

  override def save(game: String): Try[Unit] = Try {
    import java.io._
    val pw = new PrintWriter(new File("game.json"))
    pw.write(game)
    pw.close()
  }