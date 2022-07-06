package com.github.nort3x.backendchallenge2.services

import com.github.nort3x.backendchallenge2.model.Tile
import com.github.nort3x.backendchallenge2.model.Maze
import org.springframework.stereotype.Service

@Service
class MazeParser {
    fun parseMaze(mazeAsString: String): Maze {

        // region some pre-checks
        val rows = mazeAsString.split("\n")

        if (rows.isEmpty())
            throw IllegalArgumentException("should contain at least one row")

        val rowSize = rows.size
        val colSize = rows[0].length

        for ((index, row) in rows.withIndex()) {
            if (row.length != colSize)
                throw IllegalArgumentException("row $index length doesn't match first row $colSize")
        }

        if (colSize == 0)
            throw IllegalArgumentException("column size is zero and it should be at least 1 (first row)")

        // endregion


        var entrance: Tile? = null
        val walls: MutableSet<Tile> = mutableSetOf()

        for ((i, row) in rows.withIndex())
            for ((j, col) in row.withIndex())
                when (col) {
                    'E' -> {
                        if (entrance != null)
                            throw IllegalArgumentException("second entrance detected index: $i,$j")
                        else
                            entrance = Tile(i, j)
                    }
                    'W' -> walls.add(Tile(i, j))
                }

        if (entrance == null)
            throw IllegalArgumentException("no entrance provided")

        return Maze(rowSize, colSize, entrance, walls)
    }

    fun serializeMaze(maze: Maze, emptyToken: Char = ' '): String {
        val stringBuilder = StringBuilder()
        for (i in 0 until maze.gridHeight) {
            for (j in 0 until maze.gridWidth) {
                stringBuilder.append(
                    when {
                        maze.entrance == Tile(i, j) -> 'E'
                        maze.walls.contains(Tile(i, j)) -> 'W'
                        else -> emptyToken
                    }
                )
            }
            if (i + 1 != maze.gridHeight)
                stringBuilder.append('\n')
        }
        return stringBuilder.toString()
    }
}