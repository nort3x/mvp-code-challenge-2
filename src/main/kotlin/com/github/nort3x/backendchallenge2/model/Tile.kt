package com.github.nort3x.backendchallenge2.model

import com.github.nort3x.backendchallenge2.utils.alpha2Int
import javax.persistence.Embeddable

@Embeddable
class Tile(
    var rowCoord: Int,
    var colCoord: Int
) : java.io.Serializable {

    fun asGridCoord(): String =
        "${(rowCoord + 97).toChar().uppercase()}$colCoord"

    override fun toString(): String {
        return "Element(row=$rowCoord, col=$colCoord)"
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Tile

        if (rowCoord != other.rowCoord) return false
        if (colCoord != other.colCoord) return false

        return true
    }

    override fun hashCode(): Int {
        var result = rowCoord
        result = 31 * result + colCoord
        return result
    }

    companion object {
        fun fromString(asString: String): Tile =
            if (asString.length != 2) throw IllegalArgumentException()
            else Tile(asString[0].alpha2Int(), asString.substring(1).toInt())
    }


}
