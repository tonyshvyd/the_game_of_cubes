package server

import server.battle_area.BattleArea
import server.battle_area.PlayerView
import server.unit.Position
import server.unit.PositionOffset


fun main() {
    val p1 = Player("player_1")
    val p2 = Player("player_2")

    val match = Match(BattleArea(9, 6));
    var p1View = match.register(p1);
    var p2View = match.register(p2);

    printView(p1View, p2View)

    p1View = match.action(p1.id, Action.Move(1, PositionOffset(1, -1)))
    p2View = match.action(p2.id, Action.Move(2, PositionOffset(1, 1))); // move unit
    p2View = match.action(p2.id, Action.Move(4, PositionOffset(2, 1)))
    p2View = match.action(p2.id, Action.Move(4, PositionOffset(2, 1)))

    printView(p1View, p2View)

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

