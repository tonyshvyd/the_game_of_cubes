package server

import comms_model.Command
import io.ktor.serialization.deserialize
import io.ktor.server.application.install
import io.ktor.server.application.log
import io.ktor.server.cio.CIO
import io.ktor.server.engine.embeddedServer
import io.ktor.server.routing.routing
import io.ktor.server.websocket.DefaultWebSocketServerSession
import io.ktor.server.websocket.WebSockets
import io.ktor.server.websocket.receiveDeserialized
import io.ktor.server.websocket.timeout
import io.ktor.server.websocket.webSocket
import io.ktor.serialization.kotlinx.*
import io.ktor.server.websocket.converter
import io.ktor.websocket.Frame
import io.ktor.websocket.readText
import io.ktor.websocket.serialization.receiveDeserializedBase
import kotlinx.coroutines.channels.ClosedReceiveChannelException
import kotlinx.serialization.json.Json
import server.battle_area.BattleArea
import server.battle_area.PlayerView
import server.unit.Position
import server.unit.PositionOffset
import java.util.UUID


fun main() {
    embeddedServer(CIO, 6969) {
        install(WebSockets) {
            timeout
            contentConverter = KotlinxWebsocketSerializationConverter(Json)
        }

        routing {
            webSocket("/") {
            }
            webSocket("/echo") { echoRoute() }
        }
    }.start(wait = true)
//    val p1 = Player("player_1")
//    val p2 = Player("player_2")
//
//    val match = Match(BattleArea(9, 6));
//    var p1View = match.register(p1);
//    var p2View = match.register(p2);
//
//    printView(p1View, p2View)
//
//    p1View = match.action(p1.id, Action.Move(1, PositionOffset(1, -1)))
//    p2View = match.action(p2.id, Action.Move(2, PositionOffset(1, 1))); // move unit
//    p2View = match.action(p2.id, Action.Move(4, PositionOffset(2, 1)))
//    p2View = match.action(p2.id, Action.Move(4, PositionOffset(2, 1)))
//
//    printView(p1View, p2View)

//    p1View = match.action(p1.id, endTurn);
//    p2View = match.action(p2.id, endTurn);
}

suspend fun DefaultWebSocketServerSession.echoRoute() {
    val log = call.application.log;
    val id = UUID.randomUUID();
    log.debug("echo route connected: {}", id)

    for (frame in incoming) {
        var msg = converter!!.deserialize<Command.EndTurn>(frame);
        log.debug("For client - {}, received {}", id, msg);
        send(Frame.Text("Message $msg received"));
    }
    log.debug("Client {} connection closed", id)
}

fun printView(v1: PlayerView, v2: PlayerView) {
    println("============================================")
    println("Player 1");
    println(v1.render());
    println("Player 2");
    println(v2.render());
    println("============================================")
}

