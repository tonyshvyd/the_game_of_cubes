package comms_model

import kotlinx.serialization.Serializable

@Serializable
sealed class Command {
    @Serializable
    data class Move(val playerNum: Int, val unitId: Int, val row: Int, val column: Int): Command()
    @Serializable
    data class EndTurn(val playerNum: Int): Command()
    data class Unknown(val msg: String): Command()
    data class Register(val playerNum: Int): Command()
    object Quit: Command()
}