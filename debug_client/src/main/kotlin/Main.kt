package debug_client

import org.slf4j.Logger
import org.slf4j.LoggerFactory
import io.ktor.client.engine.cio.*;
import io.ktor.client.plugins.websocket.*
import io.ktor.http.*
import io.ktor.client.*
import io.ktor.websocket.*
import kotlinx.coroutines.runBlocking

fun main() {
    val logger: Logger = LoggerFactory.getLogger("debug_client")
    val client = HttpClient(CIO) {
        install(WebSockets) {
            pingIntervalMillis = 10_000
        }
    }

    runBlocking {
        client.webSocket(HttpMethod.Get, "127.0.0.1", 6969, "/") {
            logger.debug("connected to server")
            var flag = false;
            for(frame in incoming) {
                val msg =  frame as Frame.Text
                logger.debug("received {}", msg.readText());

                if (!flag) {
                    val toSend = "Hello Server!"
                    logger.debug("sending {}", toSend)
                    flag = true;
                    send(toSend)
                } else {
                    logger.debug("sending close")
                    close(CloseReason(CloseReason.Codes.NORMAL, "Bye"))
                }
            }
        }
    }
    client.close()
}