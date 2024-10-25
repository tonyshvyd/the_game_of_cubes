package server.unit

import java.util.UUID

class FootSoldier(val id: Short, val playerId: UUID, private var position: Position) : GameUnit {
    private var newPosition: Position = position.copy();
    private val moveArea: MoveArea = MoveArea(position)

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
        return newPosition;
    }

    override fun stringCode(): String {
        return "F$id";
    }

    // square around initial position
    private inner class MoveArea(position: Position) {
        private var leftBottom: Position = Position(position.row-1, position.column-1)
        private var topRight: Position = Position(position.row+1, position.column+1)

        fun contains(position: Position): Boolean {
            return (position.row >= leftBottom.row && position.row <= topRight.row)
                    && (position.column >= leftBottom.column && position.column <= topRight.column)
        }

    }
}
