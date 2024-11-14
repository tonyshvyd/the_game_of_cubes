package debug_client

sealed class Command {
    data class Move(val playerNum: Int, val unitId: Int, val row: Int, val column: Int): Command()
    data class EndTurn(val playerNum: Int): Command()
    data class Unknown(val msg: String): Command()
    object Quit: Command()

    companion object Parser {
        fun parse(msg: String): Command {
            try {
                return when {
                    msg.startsWith("q") -> return Quit
                    msg.startsWith("m") -> {
                        val (playerNum, unitId, row, column) = msg.split(",").drop(1).map { s -> s.toInt() }
                        return Move(playerNum, unitId, row, column);
                    }
                    msg.startsWith("e") -> {
                        val (playerNum) = msg.split(",").drop(1).map { s -> s.toInt() }
                        return EndTurn(playerNum)
                    }
                    else -> Unknown(msg)
                }
            } catch (e: Exception) {
                return Unknown("Failed to parse command $msg; failure: ${e.message}")
            }
        }
    }
}