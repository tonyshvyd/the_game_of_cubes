package server

import io.ktor.server.application.install
import io.ktor.server.application.log
import io.ktor.server.cio.CIO
import io.ktor.server.engine.embeddedServer
import io.ktor.server.routing.routing
import io.ktor.server.websocket.WebSockets
import io.ktor.server.websocket.timeout
import io.ktor.server.websocket.webSocket
import io.ktor.websocket.Frame
import io.ktor.websocket.readText
import kotlinx.coroutines.channels.ClosedReceiveChannelException
import server.battle_area.BattleArea
import server.battle_area.PlayerView
import server.unit.Position
import server.unit.PositionOffset


fun main() {
    embeddedServer(CIO, 6969) {
        install(WebSockets) {
            timeout
        }
        log.debug("Starting server...")

        routing {
            webSocket("/") {
                log.debug("Client connected! {}", call.request)
                send(Frame.Text("Hello client!"))

                try {
                    for (frame in incoming) {
                        var msg = (frame as Frame.Text).readText();
                        log.debug("received {}", msg);

                        send(Frame.Text("Go away"))
                    }

                    log.debug("Connection closed")
                } catch (e: ClosedReceiveChannelException) {
                    log.debug("Connection closed: {}", e.message)
                }
            }
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

fun printView(v1: PlayerView, v2: PlayerView) {
    println("============================================")
    println("Player 1");
    println(v1.render());
    println("Player 2");
    println(v2.render());
    println("============================================")
}

