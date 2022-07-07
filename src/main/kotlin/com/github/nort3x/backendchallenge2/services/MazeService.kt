package com.github.nort3x.backendchallenge2.services

import com.github.nort3x.backendchallenge2.model.*
import com.github.nort3x.backendchallenge2.repo.MazeRepo
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.util.stream.Stream
import kotlin.math.abs

@Service
class MazeService(val mazeRepo: MazeRepo, val mazeParser: MazeParser) {

    fun registerNewMaze(serializedMaze: String, owner: MazeUser): Maze =
        mazeParser.parseMaze(serializedMaze)
            .apply {
                this.owner = owner
            }.saveMaze()

    fun registerNewMaze(mazeRegisterDto: MazeRegisterDto, owner: MazeUser): Maze =
        Maze(mazeRegisterDto.gridSize, mazeRegisterDto.entrance, mazeRegisterDto.walls, owner).saveMaze()


    private fun Maze.saveMaze(): Maze {
        return mazeRepo.save(this)
    }

    fun mazesOfUser(mazeUser: MazeUser): Stream<Maze> {
        return mazeRepo.allMazesOfUserByUsername(mazeUser.username)
    }

    fun getMazeById(mazeId: Long): Maze {
        return mazeRepo.findByIdOrNull(mazeId) ?: throw IllegalArgumentException("maze not found")
    }

    @Transactional
    fun connectPath(solution: MazeSolution): MazePath {
        var entrance = solution.maze.entrance
        val linkedPath = mutableListOf<Tile>()

        fun Tile.isInNeighborOf(tile: Tile): Boolean =
            abs(this.colCoord - tile.colCoord) <= 1 && abs(this.rowCoord - tile.rowCoord) <= 1

        val solutionCopy = solution.clone()

        while (solutionCopy.path.isNotEmpty()){
            linkedPath.add(entrance)
            solutionCopy.path.remove(entrance)
            try {
                entrance = solutionCopy.path.first { entrance.isInNeighborOf(it) }

            }catch (_: java.util.NoSuchElementException){
                throw InternalError("malformed solution detected for mazeId: ${solution.maze.mazeId}")
            }
        }

        return MazePath(linkedPath)
    }


}