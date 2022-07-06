package com.github.nort3x.backendchallenge2

import com.github.nort3x.backendchallenge2.repo.UserRepo
import org.springframework.data.repository.findByIdOrNull
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.stereotype.Component

@Component
class UserDetailServiceImpl(val userRepo: UserRepo) : UserDetailsService {
    override fun loadUserByUsername(username: String?): UserDetails =
        userRepo.findByIdOrNull(username) ?: throw UsernameNotFoundException("user not found: $username")
}