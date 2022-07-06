package com.github.nort3x.backendchallenge2.services

import com.github.nort3x.backendchallenge2.model.MazeUser
import com.github.nort3x.backendchallenge2.model.UserLoginDto
import com.github.nort3x.backendchallenge2.model.UserRegisterDto
import com.github.nort3x.backendchallenge2.repo.UserRepo
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.security.web.context.HttpSessionSecurityContextRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Isolation
import org.springframework.transaction.annotation.Transactional
import javax.servlet.http.HttpServletRequest

@Service
class UserService(
    val userRepo: UserRepo,
    val passwordEncoder: PasswordEncoder,
    val authenticationManager: AuthenticationManager
) {

    @Transactional(isolation = Isolation.READ_COMMITTED)
    fun registerNewUser(userRegisterDto: UserRegisterDto): MazeUser? {
        // because business logic is simple in this example i don't relay on exception (and exception management)
        if (userRepo.findById(userRegisterDto.username).isPresent)
            return null

        return userRepo.save(MazeUser(userRegisterDto.username, passwordEncoder.encode(userRegisterDto.password)))
    }

    fun loginUser(userLoginDto: UserLoginDto, httpServletRequest: HttpServletRequest): Boolean {
        val auth = authenticationManager.authenticate(
            UsernamePasswordAuthenticationToken(
                userLoginDto.username,
                userLoginDto.password
            )
        )

        if (!auth.isAuthenticated)
            return false

        SecurityContextHolder.getContext().authentication = auth
        httpServletRequest.getSession(true).setAttribute(
            HttpSessionSecurityContextRepository.SPRING_SECURITY_CONTEXT_KEY,
            SecurityContextHolder.getContext()
        )
        return true
    }
}