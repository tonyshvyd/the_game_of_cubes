package server.unit

data class Position(var row: Int, var column: Int)

data class PositionOffset(val row: Int, val column: Int)

operator fun PositionOffset.unaryMinus() = PositionOffset(-row, column)
