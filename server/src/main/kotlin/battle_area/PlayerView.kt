package server.battle_area

import server.unit.GameUnit
import server.unit.Position
import server.unit.PositionOffset

class PlayerView(val rows: Int, val columns: Int, val units: MutableList<GameUnit>) {
    private val tiles: Array<Tile> = Array(rows * columns) { i -> Tile(i) };

    fun moveUnit(id: Short, offset: PositionOffset) {
        val unit = units.find { u -> u.id() == id } ?: throw IllegalArgumentException("Unknown unit")
        val startTile = getTile(unit.position())
        val currentTile = getTile(unit.newPosition())
        unit.move(offset);

        if (!isPositionOnBoard(unit.newPosition())) {
            throw IllegalArgumentException("Invalid new position: unit is out of field")
        }

        val targetTile = getTile(unit.newPosition())

        check(targetTile.unit == null) { "Target position is already occupied by unit ${targetTile.unit!!.stringCode()}" }

        startTile.unit = null
        currentTile.unit = null
        targetTile.unit = unit
    }

    fun update() {
        updateUnits()
    }

    private fun updateUnits() {
        units.forEach { u -> getTile(u.position()).unit = u }
    }

    private fun getTile(position: Position): Tile {
        return tiles[position.row*columns + position.column];
    }

    private fun isPositionOnBoard(position: Position): Boolean {
        return (0..<rows).contains(position.row) && (0..<columns).contains(position.column)
    }

    fun render(): String {
        val sb = StringBuilder();
        for (row in 0..<rows) {
            for (col in 0..<columns) {
                val tile = tiles[row*columns + col]
                sb.append("%-3s".format(tile))
            }
            sb.append("\n");
        }

        return sb.toString();
    }
}