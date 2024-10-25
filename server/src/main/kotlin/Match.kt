package server

import server.battle_area.BattleArea
import server.battle_area.PlayerView
import server.unit.PositionOffset
import server.unit.unaryMinus
import java.util.UUID

class Match(val battleArea: BattleArea) {
    private lateinit var p1: Player;
    private lateinit var p2: Player;
    fun register(player: Player): PlayerView {
        check(!(::p1.isInitialized && ::p2.isInitialized)) { "Match fulfilled" }

        if (::p1.isInitialized) {
            p2 = player
            player.pNum = Player.PlayerNum.TWO
            battleArea.setupPlayerTwoUnits(player)
        } else {
            p1 = player
            player.pNum = Player.PlayerNum.ONE
            battleArea.setupPlayerOneUnits(player)
        }

        return player.playerView
    }

    fun action(id: UUID, action: Action): PlayerView {
        val player = getPlayer(id)
        if (player.endedTurn()) {
            return player.playerView
        }

        when (action) {
            is Action.Move -> {
                val offset = if (player.pNum == Player.PlayerNum.TWO) { -action.offset } else { action.offset }
                player.playerView.moveUnit(action.unitId, offset)
            }
            is Action.EndTurn -> {
                player.endTurn()
            }
        }

        return player.playerView
    }

    private fun getPlayer(id: UUID): Player {
        if (p1.id == id) {
            return p1;
        }
        if (p2.id == id) {
            return p2;
        }

        throw IllegalArgumentException("Unknown player")
    }
}

private enum class Phase {
    MOVE, BATTLE, END
}

