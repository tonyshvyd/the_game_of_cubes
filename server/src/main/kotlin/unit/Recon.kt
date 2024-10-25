package server.unit

import java.util.UUID
import kotlin.math.abs

class Recon(val id: Short, val playerId: UUID, private var position: Position) : GameUnit{
    private var newPosition: Position = position.copy();
    private val moveArea: MoveArea = MoveArea(position);

    override fun id(): Short {
        return id;
    }

    override fun playerId(): UUID {
        return playerId;
    }

    override fun position(): Position {
        return position;
    }

    override fun move(offset: PositionOffset) {
        val newPosition = Position(newPosition.row + offset.row, newPosition.column + offset.column)
        if (moveArea.contains(newPosition)) {
            this.newPosition = newPosition
        } else {
            throw IllegalStateException("Position: (${newPosition.row},${newPosition.column}) is out of range")
        }
    }

    override fun newPosition(): Position {
        return newPosition
    }

    override fun stringCode(): String {
        return "R$id";
    }

    // diamond with center in position
    private class MoveArea(position: Position) {
        private var center: Position = position;
        private val max: Int = 3;

        fun contains(position: Position): Boolean {
            return abs(position.row - center.row) + abs(position.column - center.column) == max;
        }
    }
}


