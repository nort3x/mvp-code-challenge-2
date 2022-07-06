package com.github.nort3x.backendchallenge2.model

import com.fasterxml.jackson.annotation.JsonIgnore
import javax.persistence.*

@Entity
class Maze(
    @JsonIgnore
    val gridHeight: Int,
    @JsonIgnore
    val gridWidth: Int,

    @Embedded
    @AttributeOverrides(
        AttributeOverride(name = "rowCoord", column = Column(name = "entrance_row_coord")),
        AttributeOverride(name = "colCoord", column = Column(name = "entrance_col_coord"))
    )
    val entrance: Tile,

    @ElementCollection
    val walls: Set<Tile> = mutableSetOf(),

    @ManyToOne
    @JsonIgnore
    var owner: MazeUser? = null
) {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var mazeId: Long? = null

    @Embedded
    @AttributeOverrides(
        AttributeOverride(name = "rowCoord", column = Column(name = "exit_row_coord")),
        AttributeOverride(name = "colCoord", column = Column(name = "exit_col_coord"))
    )
    var exit: Tile? = null

    val gridSize: String = "${gridWidth}x${gridHeight}"

    constructor(gridSize: String, entrance: Tile, walls: Set<Tile>, owner: MazeUser? = null) : this(
        gridSize.split("x")[0].toInt(),
        gridSize.split("x")[1].toInt(),
        entrance,
        walls,
        owner
    )

    @PrePersist
    @PreUpdate
    private fun validateElements() {

        if (entrance in walls)
            throw IllegalArgumentException("one wall equals entrance")

        walls.forEach {
            if (it.colCoord > gridWidth || it.rowCoord > gridHeight)
                throw IllegalArgumentException("wall coordinate is not inside grid")
        }

        entrance.let {
            if (it.colCoord > gridWidth || it.rowCoord > gridHeight)
                throw IllegalArgumentException("entrance coordinate is not inside grid")
        }

        // search for exit point (unique)

        for (i in 0 until gridHeight) {
            if (Tile(i, gridWidth - 1) !in walls && Tile(i, gridWidth - 1) != entrance) {
                if (exit != null)
                    throw IllegalArgumentException("found second exit point at : $i,${gridWidth - 1}")
                exit = Tile(i, gridWidth - 1)
            }
            if (Tile(i, 0) !in walls && Tile(i, 0) != entrance) {
                if (exit != null)
                    throw IllegalArgumentException("found second exit point at : $i,0")
                exit = Tile(i, 0)
            }
        }
        for (j in 1 until gridWidth-1) {
            if (Tile(gridHeight - 1, j) !in walls && Tile(gridHeight - 1, j) != entrance) {
                if (exit != null)
                    throw IllegalArgumentException("found second exit point at : ${gridHeight - 1},$j")
                exit = Tile(gridHeight - 1, j)
            }
            if (Tile(0, j) !in walls && Tile(0, j) != entrance) {
                if (exit != null)
                    throw IllegalArgumentException("found second exit point at : 0,$j")
                exit = Tile(0, j)
            }

        }


        if (exit == null)
            throw IllegalArgumentException("no exit point provided")

        if (exit in setOf(
                Tile(0, 0),
                Tile(gridHeight - 1, 0),
                Tile(gridHeight - 1, gridWidth - 1),
                Tile(0, gridWidth - 1),
            )
        )
            throw IllegalArgumentException("exit point can't be located at edges")

        if (entrance in setOf(
                Tile(0, 0),
                Tile(gridHeight - 1, 0),
                Tile(gridHeight - 1, gridWidth - 1),
                Tile(0, gridWidth - 1),
            )
        )
            throw IllegalArgumentException("entrance point can't be located at edges")
    }

}

data class MazeRegisterDto(val gridSize: String, val entrance: Tile, val walls: Set<Tile> = setOf())