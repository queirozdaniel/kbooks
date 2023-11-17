package dev.danielqueiroz.kbooks

import com.typesafe.config.ConfigFactory

data class WebappConfig(
    val httpPort: Int
)

fun createAppConfig(env: String) = ConfigFactory
    .parseResources("app-${env}.conf")
    .withFallback(ConfigFactory.parseResources("app.conf"))
    .resolve().let {
        WebappConfig(
            httpPort = it.getInt("httpPort"),
        )
    }