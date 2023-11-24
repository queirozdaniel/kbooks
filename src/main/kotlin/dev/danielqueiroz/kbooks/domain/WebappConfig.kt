package dev.danielqueiroz.kbooks.domain

data class WebappConfig(
    val httpPort: Int,
    val dbUser: String,
    val dbPassword: String,
    val dbUrl: String
)
