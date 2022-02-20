package io.github.mutsuhiro6.scalate.template

import scala.util.control.Exception.allCatch
import org.fusesource.scalate.TemplateEngine
import scala.io.Source
import scala.collection.mutable

trait TemplateVariables:
  def toMap: Map[String, String]

case class TrialTemplateVariables(title: String, content: String)
    extends TemplateVariables:
  override def toMap: Map[String, String] =
    Map("title" -> title, "content" -> content)

def applyTemplate(
    variables: TemplateVariables,
    outputPath: String
): Either[Throwable, String] = allCatch.either {
  val engine = new TemplateEngine
  val header = readTextFile("inputs/header.html")
  val sidebar = readTextFile("inputs/sidebar.html")
  val vari = mutable.Map("header" -> header, "sidebar" -> sidebar)
  vari.addAll(variables.toMap)
  engine.layout("inputs/template.mustache", vari.toMap)
}

private[template] def readTextFile(path: String): String =
  val source = Source.fromFile(path)
  val text = source.getLines.mkString("\n")
  source.close
  text
