package com.github.nort3x.backendchallenge2.controller

import com.github.nort3x.backendchallenge2.configuration.perms.Authenticated
import com.github.nort3x.backendchallenge2.model.Maze
import com.github.nort3x.backendchallenge2.model.MazePath
import com.github.nort3x.backendchallenge2.model.MazeRegisterDto
import com.github.nort3x.backendchallenge2.services.MazeService
import com.github.nort3x.backendchallenge2.services.SecurityService
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.transaction.annotation.Transactional
import org.springframework.web.bind.annotation.*

@RequestMapping("/v1/maze")
@RestController
class MazeController(private val mazeService: MazeService, private val securityService: SecurityService) {
    @PostMapping
    @Authenticated
    fun registerNewMaze(@RequestBody mazeRegisterDto: MazeRegisterDto) =
        ResponseEntity.status(HttpStatus.CREATED)
            .body(mazeService.registerNewMaze(mazeRegisterDto, securityService.currentUserSafe()))

    @PostMapping("/serial")
    @Authenticated
    fun registerNewMaze(@RequestBody mazeRegisterDto: String) =
        ResponseEntity.status(HttpStatus.CREATED)
            .body(mazeService.registerNewMaze(mazeRegisterDto, securityService.currentUserSafe()))

    @GetMapping
    @Authenticated
    @Transactional
    fun retrieveMazes(): ResponseEntity<List<Maze>> =
        ResponseEntity.ok(
            mazeService.mazesOfUser(securityService.currentUserSafe())
                .limit(100)
                .toList()
        )

    @GetMapping("/{mazeId}/solution")
    @Authenticated
    @Transactional
    fun getSolution(@RequestParam mode: SolutionMode, @PathVariable mazeId: Long): MazePath {
        val maze: Maze = getMaze(mazeId)

        if (!maze.solved)
            throw IllegalArgumentException("maze not solved yet, please wait for a minute")
        if (maze.mazeSolutions.isEmpty())
            throw IllegalArgumentException("maze doesn't have any solution")
        val solution = when (mode) {
            SolutionMode.min -> maze.mazeSolutions.minByOrNull { it.path.size }
            SolutionMode.max -> maze.mazeSolutions.maxByOrNull { it.path.size }
        }!!

        return mazeService.connectPath(solution)
    }

    @GetMapping("/{mazeId}")
    @Authenticated
    @Transactional
    fun getMaze(@PathVariable mazeId: Long): Maze {
        val maze: Maze = mazeService.getMazeById(mazeId)
        if (maze.owner != securityService.currentUserSafe())
            throw IllegalArgumentException("you are not owner of this maze")
        return maze
    }
}

enum class SolutionMode {
    min,
    max
}