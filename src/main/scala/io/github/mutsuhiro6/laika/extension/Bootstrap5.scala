package io.github.mutsuhiro6.laika.extension

import laika.ast._
import laika.bundle.ExtensionBundle
import laika.format.HTML
import laika.render.HTMLFormatter

object Bootstrap5 extends ExtensionBundle:

  private var tableOpts: TableOptions = TableOptions(
    false,
    false,
    false,
    false,
    Set.empty[String]
  )

  case class TableOptions(
      striped: Boolean,
      smallen: Boolean,
      hoverable: Boolean,
      responsive: Boolean,
      additionalOpts: Set[String]
  )

  def configurate(tableOpts: TableOptions): Unit =
    this.tableOpts = tableOpts

  override def description: String =
    "Adapt HTML outputs to Bootstrap v5.0 syntax."

  override def useInStrictMode: Boolean = true

  override def renderOverrides = Seq(
    HTML.Overrides(noClassHeader),
    HTML.Overrides(table),
    HTML.Overrides(tableElem),
    HTML.Overrides(responsiveImg)
  )

  private val table: PartialFunction[(HTMLFormatter, Element), String] =
    case (fmt, Table(head, body, caption, columns, options)) =>
      val children =
        Seq(caption, columns, head, body).filterNot(_.content.isEmpty)
      val styles = scala.collection.mutable.Set("table")
      if tableOpts.striped then styles += "table-striped"
      if tableOpts.smallen then styles += "table-sm"
      if tableOpts.hoverable then styles += "table-hover"
      if tableOpts.responsive then styles += "table-responsive"
      styles ++= tableOpts.additionalOpts
      val newOpt = Options(options.id, styles.toSet)
      fmt.indentedElement("table", newOpt, children)

  // TODO: row-wise header adaption
  private val tableElem: PartialFunction[(HTMLFormatter, Element), String] =
    case (
          fmt,
          Cell(HeadCell, Seq(Paragraph(spans, opt)), colspan, rowspan, opts)
        ) =>
      fmt.element(
        "th",
        opts,
        Seq(SpanSequence(spans, opt)),
        fmt.optAttributes(
          "scope" -> Some("col"),
          "colspan" -> noneIfDefault(colspan, 1),
          "rowspan" -> noneIfDefault(rowspan, 1)
        ): _*
      )

  private val noClassHeader: PartialFunction[(HTMLFormatter, Element), String] =
    case (fmt, Header(level, content, opt)) =>
      val newOpt = Options(opt.id, Set())
      fmt.newLine + fmt.element(s"h$level", newOpt, content)

  private val responsiveImg: PartialFunction[(HTMLFormatter, Element), String] =
    case (fmt, Image(target, _, _, alt, title, opt)) =>
      val imgSrc = fmt.pathTranslator.translate(target) match
        case ext: ExternalTarget => ext.url
        case int: InternalTarget =>
          int.relativeTo(fmt.path).relativePath.toString
      val allAttr = fmt.optAttributes(
        "src" -> Some(imgSrc),
        "alt" -> alt,
        "title" -> title,
        "class" -> Some("img-fluid")
      )
      fmt.emptyElement("img", opt, allAttr: _*)

  private def noneIfDefault[T](actual: T, default: T): Option[String] =
    if (actual == default) None else Some(actual.toString)
