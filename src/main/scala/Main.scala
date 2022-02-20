import io.github.mutsuhiro6.laika.`extension`._
import io.github.mutsuhiro6.scalate.template._
import scala.io.Source
import laika.ast.Document
import laika.api.{MarkupParser, Renderer}
import laika.format.{HTML, Markdown}
import laika.markdown.github.GitHubFlavor
import laika.ast.MessageFilter
import laika.config.Config.ConfigResult
import scala.util.control.Exception.allCatch
import laika.ast.Path
import io.github.mutsuhiro6.laika.config.MyConfig

@main def hello: Unit =
  val input = Source.fromFile("inputs/test.md", "utf-8").getLines.mkString("\n")
  val out = for
    doc <- parseMd(input)
    config <- getConfig(doc)
    html <- renderAsHtml(doc)
    out <- applyTemplate(
      TrialTemplateVariables(config.title, html),
      "output.html"
    )
  yield out
  out.foreach { cc =>
    val pw = new java.io.PrintWriter("output.html")
    pw.print(cc)
    pw.close
  }

def parseMd(input: String): Either[Throwable, Document] =

  val parser = MarkupParser
    .of(Markdown)
    .using(GitHubFlavor, ImagePathRewriter)
    .build
  parser.parse(input, Path.parse("/inputs"))

def getConfig(input: Document): ConfigResult[MyConfig] =
  import io.github.mutsuhiro6.laika.config.MyConfig._
  input.config.get[MyConfig](configDecorder, defaultKey)

def renderAsHtml(input: Document): Either[Throwable, String] =
  import Bootstrap5.TableOptions
  val tblOpt = TableOptions(true, false, true, false, Set())
  Bootstrap5.configurate(tblOpt)

  val htmlRenderer = Renderer
    .of(HTML)
    .renderMessages(MessageFilter.Error)
    .using(HighlightJs, Bootstrap5, MermaidJs)
    .build
  allCatch.either(htmlRenderer.render(input))
