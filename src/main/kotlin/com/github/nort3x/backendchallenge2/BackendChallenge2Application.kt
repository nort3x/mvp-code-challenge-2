package com.github.nort3x.backendchallenge2

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.scheduling.annotation.EnableScheduling

@SpringBootApplication
@EnableScheduling
class BackendChallenge2Application

fun main(args: Array<String>) {
    runApplication<BackendChallenge2Application>(*args)
}
