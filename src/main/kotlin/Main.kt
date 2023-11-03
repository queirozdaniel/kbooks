import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*
import io.ktor.server.plugins.statuspages.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import org.slf4j.Logger
import org.slf4j.LoggerFactory

val log: Logger? = LoggerFactory.getLogger("Main")
fun main(args: Array<String>) {
    embeddedServer(Netty, port = 4207) {
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
        get("/") {
            call.respondText("Hello, World!")
        }
    }

}