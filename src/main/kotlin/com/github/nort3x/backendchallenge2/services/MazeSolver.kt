package com.github.nort3x.backendchallenge2.services

import com.github.nort3x.backendchallenge2.model.Maze
import com.github.nort3x.backendchallenge2.model.MazeSolution
import com.github.nort3x.backendchallenge2.model.Tile
import com.github.nort3x.backendchallenge2.repo.MazeRepo
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.util.stream.IntStream
import kotlin.streams.asSequence

@Service
/**
 * @see <p> heavily inspired from <a href="https://kalkicode.com/find-solutions-maze">kalkicode</a> </p>
 */
class MazeSolver(val mazeParser: MazeParser, val mazeRepo: MazeRepo) {

    @Transactional
    fun solveMaze(mazeId: Long): Maze = runBlocking {

        val maze = mazeRepo.findByIdOrNull(mazeId) ?: throw IllegalArgumentException("maze not found, mazeId: $mazeId")

        val mazeString = mazeParser.serializeMaze(maze, 'X')

        val mazeMatrix = mazeString.lineSequence().map { it.toCharArray() }.map {
            it.map { char ->
                when (char) {
                    MazeParser.WALL_CHAR -> 0
                    else -> 1
                }
            }
        }.map { it.toIntArray() }.toList().toTypedArray()

        val backtrackMatrix = IntStream.range(0, maze.gridHeight).asSequence().map {
            BooleanArray(maze.gridWidth) { false }
        }.toList().toTypedArray()

        val solutions = mutableSetOf<MazeSolution>()
        findAllMazeSolutions(
            mazeMatrix,
            backtrackMatrix,
            maze,
            MazeSolution(maze, mutableSetOf(Tile(maze.entrance.rowCoord, maze.entrance.colCoord))),
            solutions,
            maze.entrance.rowCoord, maze.entrance.colCoord
        )

        maze.mazeSolutions.addAll(solutions)
        maze.solved = true

        withContext(Dispatchers.IO) {
            mazeRepo.save(maze)
        }
    }

    private fun findAllMazeSolutions(
        mazeMatrix: Array<IntArray>,
        backtrackMatrix: Array<BooleanArray>,
        maze: Maze,
        currentPath: MazeSolution,
        solutions: MutableSet<MazeSolution>,
        rowCoord: Int,
        colCoord: Int
    ) {
        //When not valid position
        if (rowCoord < 0 || rowCoord >= maze.gridHeight || colCoord < 0 || colCoord >= maze.gridWidth) {
            return;
        }
        if (backtrackMatrix[rowCoord][colCoord]) {
            return;
        }
        if (rowCoord == maze.exit?.rowCoord && colCoord == maze.exit?.colCoord) {
            // found a solution
            solutions.add(currentPath)
        }
        // open position
        if (mazeMatrix[rowCoord][colCoord] == 1) {
            currentPath.path.add(Tile(rowCoord, colCoord))
            backtrackMatrix[rowCoord][colCoord] = true
            findAllMazeSolutions(
                mazeMatrix,
                backtrackMatrix,
                maze,
                currentPath.clone(),
                solutions,
                rowCoord + 1,
                colCoord
            );
            findAllMazeSolutions(
                mazeMatrix,
                backtrackMatrix,
                maze,
                currentPath.clone(),
                solutions,
                rowCoord,
                colCoord + 1
            );
            findAllMazeSolutions(
                mazeMatrix,
                backtrackMatrix,
                maze,
                currentPath.clone(),
                solutions,
                rowCoord - 1,
                colCoord + 1
            );
            findAllMazeSolutions(
                mazeMatrix,
                backtrackMatrix,
                maze,
                currentPath.clone(),
                solutions,
                rowCoord,
                colCoord - 1
            );
            backtrackMatrix[rowCoord][colCoord] = false
        }


    }


}