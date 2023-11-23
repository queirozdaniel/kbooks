package dev.danielqueiroz.kbooks

import com.typesafe.config.ConfigFactory
import dev.danielqueiroz.kbooks.domain.*
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*
import io.ktor.server.plugins.statuspages.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import io.ktor.util.pipeline.*
import org.slf4j.Logger
import org.slf4j.LoggerFactory

val log: Logger? = LoggerFactory.getLogger("Main")
fun main(args: Array<String>) {

    log?.debug("Starting application...")

    val env = System.getenv("KOTLINBOOK_ENV") ?: "local"
    log?.debug("Loading configuration for environment $env")

    val config = ConfigFactory
        .parseResources("app-${env}.conf")
        .withFallback(ConfigFactory.parseResources("app.conf"))
        .resolve().let {
            WebappConfig(
                httpPort = it.getInt("httpPort"),
            )
        }

    embeddedServer(Netty, port = config.httpPort) {
        createKtorApplication()
    }.start(wait = true)
}

fun Application.createKtorApplication() {
    install(StatusPages) {
        exception<Throwable> { call, cause ->
            this@createKtorApplication.log.error("An unknown error occurred", cause)

            call.respondText(
                text = "500: $cause",
                status = HttpStatusCode.InternalServerError
            )
        }
    }
    routing {
        get("/", webResponse {
            TextWebResponse("Hello, World!")
        })

        get("/param_test", webResponse {
            TextWebResponse(
                "The param is: ${call.request.queryParameters["foo"]}"
            )
        })
        get("/json_test", webResponse {
            JsonWebResponse(mapOf("foo" to "bar"))
        })
        get("/json_test_with_header", webResponse {
            JsonWebResponse(mapOf("foo" to "bar"))
                .header("X-Test-Header", "Just a test!")
        })
    }

}

fun webResponse(
    handler: suspend PipelineContext<Unit, ApplicationCall>.() -> WebResponse
): PipelineInterceptor<Unit, ApplicationCall> {
    return {
        val resp = this.handler()
        for ((name, values) in resp.headers())
            for (value in values)
                call.response.header(name, value)
        val statusCode = HttpStatusCode.fromValue(
            resp.statusCode
        )
        when (resp) {
            is TextWebResponse -> {
                call.respondText(
                    text = resp.body,
                    status = statusCode
                )
            }

            is JsonWebResponse -> {
                call.respond(
                    KtorJsonWebResponse(
                        body = resp.body,
                        statusCode = statusCode
                    )
                )
            }
        }
    }
}