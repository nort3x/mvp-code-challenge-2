package com.github.nort3x.backendchallenge2.controller

import com.github.nort3x.backendchallenge2.configuration.perms.Authenticated
import com.github.nort3x.backendchallenge2.model.Maze
import com.github.nort3x.backendchallenge2.model.MazeRegisterDto
import com.github.nort3x.backendchallenge2.services.MazeService
import com.github.nort3x.backendchallenge2.services.SecurityService
import org.springframework.http.ResponseEntity
import org.springframework.transaction.annotation.Transactional
import org.springframework.web.bind.annotation.*
import java.util.stream.Stream

@RequestMapping("/v1/maze")
@RestController
class MazeController(private val mazeService: MazeService, private val securityService: SecurityService) {
    @PostMapping
    @Authenticated
    fun registerNewMaze(@RequestBody mazeRegisterDto: MazeRegisterDto) =
        mazeService.registerNewMaze(mazeRegisterDto, securityService.currentUserSafe())

    @PostMapping("/serial")
    @Authenticated
    fun registerNewMaze(@RequestBody mazeRegisterDto: String) =
        mazeService.registerNewMaze(mazeRegisterDto, securityService.currentUserSafe())

    @GetMapping
    @Authenticated
    @Transactional
    fun retrieveMazes(): ResponseEntity<List<Maze>> =
        ResponseEntity.ok(
            mazeService.mazesOfUser(securityService.currentUserSafe())
                .limit(1000)
                .toList()
        )
}