package com.github.nort3x.backendchallenge2.cron

import com.github.nort3x.backendchallenge2.repo.MazeRepo
import com.github.nort3x.backendchallenge2.services.MazeSolver
import org.slf4j.LoggerFactory
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional
import java.util.concurrent.TimeUnit
import kotlin.streams.asSequence

@Component
class MazeSolvingCron(val mazeRepo: MazeRepo, val mazeSolver: MazeSolver) {
    @Scheduled(fixedRate = 1, timeUnit = TimeUnit.SECONDS)
    @Transactional
    fun solvedSomeUnsolvedMazes() {
        mazeRepo.streamOfUnsolvedMazes().parallel()
            .limit(20)
            .forEach {
                mazeSolver.solveMaze(it.mazeId!!)
                LoggerFactory.getLogger(javaClass).info("solved maze: ${it.mazeId}")
            }
    }
}