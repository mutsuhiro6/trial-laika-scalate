package io.github.mutsuhiro6.laika.config

import laika.config.ConfigDecoder
import laika.config.DefaultKey

object MyConfig:
  val configDecorder: ConfigDecoder[MyConfig] =
    ConfigDecoder.config.flatMap { config =>
      for
        title <- config.get[String]("title")
        date <- config.get[String]("date")
        author <- config.get[String]("author")
      yield MyConfig(title, date, author)
    }

  val defaultKey: DefaultKey[MyConfig] = DefaultKey[MyConfig]("")

case class MyConfig(
    title: String,
    date: String,
    author: String
)
