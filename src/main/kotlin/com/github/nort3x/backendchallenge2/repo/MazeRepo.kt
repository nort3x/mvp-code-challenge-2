package com.github.nort3x.backendchallenge2.repo

import com.github.nort3x.backendchallenge2.model.Maze
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.stereotype.Repository
import java.util.stream.Stream

@Repository
interface MazeRepo : JpaRepository<Maze, Long> {
    @Query("select maze from Maze maze where maze.owner.username = :username")
    fun allMazesOfUserByUsername(username: String): Stream<Maze>
}