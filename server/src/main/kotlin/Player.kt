package server

import server.battle_area.PlayerView
import java.util.UUID

class Player(val name: String, val id: UUID) {
    lateinit var playerView: PlayerView
    lateinit var pNum: PlayerNum
    private var status: Status = Status.ACTIVE
    constructor(name: String) : this(name, UUID.randomUUID())
    constructor(name: String, id: String): this(name, UUID.fromString(id))

    fun endedTurn(): Boolean {
        return status == Status.END_TURN
    }

    fun endTurn() {
        status = Status.END_TURN
    }

    fun newTurn() {
        status = Status.ACTIVE
    }
    enum class PlayerNum { ONE, TWO }
    private enum class Status { ACTIVE, END_TURN}
}

