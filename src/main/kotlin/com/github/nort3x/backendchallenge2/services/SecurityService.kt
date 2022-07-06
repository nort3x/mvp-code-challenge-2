package com.github.nort3x.backendchallenge2.services

import com.github.nort3x.backendchallenge2.model.MazeUser
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.stereotype.Service

@Service
class SecurityService {
    fun currentUser(): MazeUser? =
        when (SecurityContextHolder.getContext().authentication.principal) {
            is MazeUser -> SecurityContextHolder.getContext().authentication.principal as MazeUser
            else -> null
        }

    fun currentUserSafe(): MazeUser =
        currentUser()!!
}
