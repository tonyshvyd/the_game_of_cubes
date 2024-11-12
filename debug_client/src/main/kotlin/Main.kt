package debug_client

import org.slf4j.Logger
import org.slf4j.LoggerFactory
import io.ktor.client.engine.cio.*;
import io.ktor.client.plugins.websocket.*
import io.ktor.http.*
import io.ktor.client.*
import io.ktor.websocket.*
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlin.random.Random

fun main() {
    val logger: Logger = LoggerFactory.getLogger("debug_client")
    val client = HttpClient(CIO) {
        install(WebSockets) {
            pingIntervalMillis = 10_000
        }
    }

    runBlocking {
        runEcho(logger, client);
    }
    client.close()
}

suspend fun runEcho(logger: Logger, client: HttpClient) {
    val host = "127.0.0.1"
    val port = 6969;
    val echo = "/echo"
    coroutineScope {
        launch {
            client.webSocket(HttpMethod.Get, host, port, echo) {
                startEcho(1, logger)
            }
        }

        launch {
            client.webSocket(HttpMethod.Get, host, port, echo) {
                startEcho(2, logger)
            }
        }
    }
}

suspend fun DefaultWebSocketSession.startEcho(clientNum: Int, logger: Logger) {
    repeat(5) {
        val sendMessage = "Message $it"
        logger.debug("Client {} Sending {}", clientNum, sendMessage)
        send(sendMessage);
        val response = incoming.receive() as Frame.Text;
        val backMessage = response.readText()
        logger.debug("Client {} received {}", clientNum, backMessage);
        delay(Random.nextLong(2000L, 10000L))
    }
    logger.debug("Closing client {}", clientNum)
    close(CloseReason(CloseReason.Codes.NORMAL, "Echo finished $clientNum"));
}