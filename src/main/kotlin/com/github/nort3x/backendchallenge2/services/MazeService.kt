package com.github.nort3x.backendchallenge2.services

import com.github.nort3x.backendchallenge2.model.Maze
import com.github.nort3x.backendchallenge2.model.MazeRegisterDto
import com.github.nort3x.backendchallenge2.model.MazeUser
import com.github.nort3x.backendchallenge2.repo.MazeRepo
import org.springframework.stereotype.Service
import java.util.stream.Stream

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


}