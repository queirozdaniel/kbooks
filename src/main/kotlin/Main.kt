import io.ktor.server.application.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun main(args: Array<String>) {
    embeddedServer(Netty, port = 4207) {
        createKtorApplication()
    }.start(wait = true)
}

fun Application.createKtorApplication() {

    routing {
        get("/") {
            call.respondText("Hello, World!")
        }
    }

}