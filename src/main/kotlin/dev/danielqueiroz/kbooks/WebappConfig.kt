package dev.danielqueiroz.kbooks

import com.typesafe.config.ConfigFactory

data class WebappConfig(
    val httpPort: Int
)

fun createAppConfig() = ConfigFactory
    .parseResources("app.conf")
    .resolve().let {
        WebappConfig(
            httpPort = 4207,
        )
    }