package server.battle_area

import server.unit.GameUnit

class Tile(val idx: Int) {
    var unit: GameUnit? = null;

    override fun toString(): String {
        return unit?.stringCode() ?: "0";
    }
}