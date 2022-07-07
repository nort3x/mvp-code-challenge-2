package com.github.nort3x.backendchallenge2

import com.github.nort3x.backendchallenge2.model.Tile
import com.github.nort3x.backendchallenge2.repo.MazeRepo
import com.github.nort3x.backendchallenge2.services.MazeParser
import com.github.nort3x.backendchallenge2.services.MazeService
import com.github.nort3x.backendchallenge2.services.MazeSolver
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertDoesNotThrow
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.annotation.DirtiesContext
import org.springframework.test.context.ActiveProfiles
import org.springframework.transaction.annotation.Transactional
import kotlin.test.assertEquals

@SpringBootTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
@ActiveProfiles("test")
class ApiTest {

    @Autowired
    lateinit var mazeParser: MazeParser


    @Test
    fun `maze parser test`() {
        val maze = """
            #######E###
            ######    #
            #    #    #
            #         #
            # #########
        """.trimIndent()

        val actualMaze = mazeParser.parseMaze(maze)

        assertEquals(actualMaze.walls.size, maze.count {
            it == 'W'
        })

        assertEquals(actualMaze.gridHeight, 3)
        assertEquals(actualMaze.gridWidth, 6)
        assertEquals(actualMaze.entrance, Tile.fromString("A3"))


        assertEquals(mazeParser.serializeMaze(actualMaze), maze)


    }


    @Autowired
    lateinit var mazeRepo: MazeRepo

    @Autowired
    lateinit var mazeSolver: MazeSolver

    @Autowired
    lateinit var mazeService: MazeService

    @Test
    fun `maze solver test`() {
        var maze = mazeParser.parseMaze(
            """
                #######E###
                ####### ###
                ######  ###
                ######  ###
                ####### ###
        """.trimIndent()
        )

        maze = mazeRepo.save(maze)


        maze = mazeSolver.solveMaze(maze.mazeId!!)
        assertEquals(maze.mazeSolutions.size, 2)

        assertDoesNotThrow {
            mazeService.connectPath(maze.mazeSolutions.first())
        }

    }
}