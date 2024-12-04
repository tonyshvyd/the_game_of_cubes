package debug_client

import comms_model.Command
import comms_model.Command.EndTurn
import comms_model.Command.Move
import comms_model.Command.Quit
import comms_model.Command.Register
import comms_model.Command.Unknown

fun parseCommand(msg: String): Command {
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
            msg.startsWith("r") -> {
                val (playerNum) = msg.split(",").drop(1).map { s -> s.toInt() }
                return Register(playerNum)
            }
            else -> Unknown(msg)
        }
    } catch (e: Exception) {
        return Unknown("Failed to parse command $msg; failure: ${e.message}")
    }
}