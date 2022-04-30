package fileIoComponent.fileIoXmlImpl

import com.google.inject.{Guice, Key}
import com.google.inject.name.Names

import scala.io.Source
import fileIoComponent.FileIOInterface
import play.api.libs.json.{JsValue, Json}

import scala.annotation.tailrec
import scala.collection.mutable.ListBuffer
import scala.util.Try
import scala.xml.{Node, PrettyPrinter}

class FileIO extends FileIOInterface:
  override def load(): Try[String] = Try { scala.xml.XML.loadFile("game.xml").toString() }

  override def save(game: String): Try[Unit] = Try {
    import java.io._
    val pw = new PrintWriter(new File("game.xml"))
    val prettyPrinter = new PrettyPrinter(120, 4)
    pw.write(game)
    pw.close()
  }