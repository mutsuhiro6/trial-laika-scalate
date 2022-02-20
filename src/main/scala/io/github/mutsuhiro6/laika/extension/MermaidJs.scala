package io.github.mutsuhiro6.laika.extension

import laika.ast._
import laika.bundle.ExtensionBundle
import laika.format.HTML
import laika.render.HTMLFormatter

object MermaidJs extends ExtensionBundle:

  override def description: String = "Enable to use marmeid.js."

  override def useInStrictMode: Boolean = true

  override def renderOverrides =
    Seq(HTML.Overrides(adaptToMarmeidJs))

  private val Mermaid = "mermaid"

  private val adaptToMarmeidJs
      : PartialFunction[(HTMLFormatter, Element), String] =
    case (fmt, CodeBlock(lang, content, _, opt)) if lang == Mermaid =>
      fmt.element("div", Options(opt.id, Set(Mermaid)), content)
