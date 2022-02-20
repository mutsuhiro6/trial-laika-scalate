package io.github.mutsuhiro6.laika.extension

import laika.ast._
import laika.ast.RewriteRules.RewriteRulesBuilder
import laika.bundle.ExtensionBundle

object ImagePathRewriter extends ExtensionBundle:
  override def description: String = "Rewrite images path for publishing."

  override def useInStrictMode: Boolean = true

  override def rewriteRules: Seq[RewriteRulesBuilder] =
    Seq(_ =>
      Right(RewriteRules.forSpans { case img: Image =>
        Replace(img.withTarget(Target.parse("inputs" + img.target.render())))
      })
    )
