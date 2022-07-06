package com.github.nort3x.backendchallenge2.controller

import com.github.nort3x.backendchallenge2.configuration.perms.OnlyAnonymous
import com.github.nort3x.backendchallenge2.model.MazeUser
import com.github.nort3x.backendchallenge2.model.UserLoginDto
import com.github.nort3x.backendchallenge2.model.UserRegisterDto
import com.github.nort3x.backendchallenge2.services.UserService
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import javax.servlet.http.HttpServletRequest

@RestController
@RequestMapping(produces = [MediaType.APPLICATION_JSON_VALUE])
class UserController(val userService: UserService) {

    @PostMapping("/user")
    @OnlyAnonymous
    fun registerUser(@RequestBody userRegisterDto: UserRegisterDto): ResponseEntity<MazeUser?> {
        userService.registerNewUser(userRegisterDto)
            ?.let {
                return ResponseEntity.status(HttpStatus.CREATED).body(it)
            }
            ?: return ResponseEntity.status(HttpStatus.CONFLICT).body(null)
    }

    @OnlyAnonymous
    @PostMapping("/login")
    fun loginUser(
        @RequestBody userLoginDto: UserLoginDto,
        httpServletRequest: HttpServletRequest
    ): ResponseEntity<Boolean> {
        val result: Boolean = userService.loginUser(userLoginDto, httpServletRequest);
        return ResponseEntity.status(if (result) 200 else 403).body(result)
    }

}