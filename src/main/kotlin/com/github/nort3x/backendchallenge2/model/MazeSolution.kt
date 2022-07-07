package com.github.nort3x.backendchallenge2.model

import com.fasterxml.jackson.annotation.JsonIgnore
import javax.persistence.*

@Entity
class MazeSolution(
    @ManyToOne
    @JsonIgnore
    val maze: Maze,

    @ElementCollection
    val path: MutableSet<Tile>
) {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var solutionId: Long? = null
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as MazeSolution

        if (solutionId == other.solutionId && solutionId != null) return true

        if (maze != other.maze) return false
        if (path != other.path) return false

        return true
    }

    override fun hashCode(): Int {
        var result = maze.hashCode()
        result = 31 * result + path.hashCode()
        result = 31 * result + (solutionId?.hashCode() ?: 0)
        return result
    }

    override fun toString(): String {
        return "MazeSolution( solutionId=$solutionId , path=$path)"
    }

    fun clone(): MazeSolution {
        return MazeSolution(maze, mutableSetOf<Tile>().apply {
            addAll(path)
        })
    }


}