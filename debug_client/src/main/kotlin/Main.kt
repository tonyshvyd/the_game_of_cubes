package debug_client

import comms_model.Command
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import io.ktor.client.engine.cio.*;
import io.ktor.client.plugins.websocket.*
import io.ktor.http.*
import io.ktor.client.*
import io.ktor.websocket.*
import io.ktor.serialization.kotlinx.*
import kotlinx.serialization.json.*
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlin.random.Random

const val HOST: String = "127.0.0.1"
const val PORT: Int = 6969
const val ECHO: String = "/echo"
const val REGISTER: String = "/register"

fun main() {
    val logger: Logger = LoggerFactory.getLogger("debug_client")
    val client = HttpClient(CIO) {
        install(WebSockets) {
            pingIntervalMillis = 10_000
            contentConverter = KotlinxWebsocketSerializationConverter(Json)
        }
    }

    val activeSessions = mutableMapOf<Int, DefaultClientWebSocketSession>()

    runBlocking {
        while (true) {
            val msg = readln();
            val command = parseCommand(msg);
            logger.debug("{}", command)
            when (command) {
                is Command.Quit -> return@runBlocking;
                is Command.Unknown -> logger.error("Unknown command: {}", command)
                is Command.Move -> {
                    activeSessions[command.playerNum]?.let {
                        it.sendSerialized(command)
                        val resp = it.incoming.receive() as Frame.Text;
                        logger.debug("Received {}", resp.readText())
                    }
                }
                is Command.Register -> {
                    if (activeSessions.contains(command.playerNum)) {
                        logger.error("Session already exists for player {}", command.playerNum)
                    } else {
                        val session = client.webSocketSession(HttpMethod.Get, HOST, PORT, "/echo")
                        activeSessions.put(command.playerNum, session)
                        logger.debug("Connected player {}, {}", command.playerNum, session)
                    }
                }
                is Command.EndTurn -> {
                    activeSessions[command.playerNum]?.let {
                        it.sendSerialized(command)
                        val resp = it.incoming.receive() as Frame.Text;
                        logger.debug("Received {}", resp.readText())
                    }
                }
            }
        }
//        runEcho(logger, client);
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