package server.unit

import java.util.UUID

interface GameUnit {
    fun id(): Short;
    fun playerId(): UUID;
    fun position(): Position;
    fun move(offset: PositionOffset);
    fun newPosition(): Position;
    fun stringCode(): String;
}