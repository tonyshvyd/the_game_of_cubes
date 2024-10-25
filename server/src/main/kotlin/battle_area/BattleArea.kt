package server.battle_area

import server.Player
import server.unit.FootSoldier
import server.unit.GameUnit
import server.unit.Position
import server.unit.Recon

class BattleArea(val rows: Int, val columns: Int) {
    private val tiles: Array<Tile> = Array(rows * columns) { i -> Tile(i) };
    private val units: MutableList<GameUnit> = mutableListOf();
    private lateinit var p1: Player;
    private lateinit var p2: Player;

    fun setupPlayerOneUnits(player: Player) {
        var index: Short = 1;
        val startRow = 0;
        val middle: Int = (columns - 1) / 2;

        units.add(FootSoldier(index++, player.id, Position(startRow, middle)))
        units.add(FootSoldier(index++, player.id, Position(startRow, middle-2)))
        units.add(FootSoldier(index++, player.id, Position(startRow, middle+2)))
        units.add(Recon(index++, player.id, Position(startRow+1, middle)))

        updateUnits()
        player.playerView = PlayerView(rows, columns, units.filter{ u -> u.playerId() == player.id}.toMutableList());
        p1 = player
        player.playerView.update();
    }

    fun setupPlayerTwoUnits(player: Player) {
        var index: Short = 1;
        val startRow = rows - 1;
        val middle: Int = (columns - 1) / 2;
        units.add(FootSoldier(index++, player.id, Position(startRow, middle)))
        units.add(FootSoldier(index++, player.id, Position(startRow, middle-2)))
        units.add(FootSoldier(index++, player.id, Position(startRow, middle+2)))
        units.add(Recon(index++, player.id, Position(startRow-1, middle)))

        updateUnits()
        player.playerView = PlayerView(rows, columns, units.filter{u -> u.playerId() == player.id}.toMutableList());
        p2 = player
        player.playerView.update();
    }

    private fun updateUnits() {
        units.forEach { u -> getTile(u.position()).unit = u }
    }

    private fun getTile(position: Position): Tile {
        return tiles[position.row*columns + position.column];
    }
}
