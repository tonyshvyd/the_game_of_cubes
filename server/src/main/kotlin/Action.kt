package server

import server.unit.PositionOffset

sealed class Action {
    data class Move(val unitId: Short, val offset: PositionOffset) : Action() {}
    object EndTurn : Action() {}
}