package com.github.nort3x.backendchallenge2

import com.github.nort3x.backendchallenge2.model.Tile
import com.github.nort3x.backendchallenge2.services.MazeParser
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.annotation.DirtiesContext
import org.springframework.test.context.ActiveProfiles
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
            WWWEWW
            W    W
            WWWWWW
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


    @Test
    fun `maze api test`(){

    }
}