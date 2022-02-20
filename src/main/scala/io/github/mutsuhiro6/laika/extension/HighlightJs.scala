package io.github.mutsuhiro6.laika.extension

import laika.ast._
import laika.bundle.ExtensionBundle
import laika.format.HTML
import laika.render.HTMLFormatter

object HighlightJs extends ExtensionBundle:

  override def description: String = "Adapt class names to highlight.js usages."

  override def useInStrictMode: Boolean = true

  override def renderOverrides =
    Seq(HTML.Overrides(skipHighlight), HTML.Overrides(specifyLanguage))

  private val skipHighlight: PartialFunction[(HTMLFormatter, Element), String] =
    case (fmt, LiteralBlock(content, opt)) =>
      fmt.rawElement(
        "pre",
        opt,
        fmt.withoutIndentation(
          _.rawElement("code", Styles(s"nohighlight"), content)
        )
      )

  private val specifyLanguage
      : PartialFunction[(HTMLFormatter, Element), String] =
    case (fmt, CodeBlock(lang, content, _, opt)) =>
      fmt.rawElement(
        "pre",
        opt,
        fmt.withoutIndentation(
          _.element("code", Styles(s"language-$lang"), content)
        )
      )
