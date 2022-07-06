package com.github.nort3x.backendchallenge2.model

import com.fasterxml.jackson.annotation.JsonIgnore
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.userdetails.UserDetails
import javax.persistence.Entity
import javax.persistence.Id
import javax.persistence.OneToMany

@Entity

class MazeUser(
    @Id private val username: String,
    private val password: String
) : UserDetails {

    @OneToMany(mappedBy = "owner")
    @JsonIgnore
    val mazes: Set<Maze> = setOf()

    @javax.persistence.Transient
    @JsonIgnore
    override fun getAuthorities(): MutableCollection<out GrantedAuthority> = mutableListOf(
        SimpleGrantedAuthority("ROLE_USER")
    )

    override fun getPassword(): String {
        return password
    }

    override fun getUsername(): String {
        return username
    }


    @javax.persistence.Transient
    @JsonIgnore
    override fun isAccountNonExpired() = true

    @javax.persistence.Transient
    @JsonIgnore
    override fun isAccountNonLocked() = true

    @javax.persistence.Transient
    @JsonIgnore
    override fun isCredentialsNonExpired() = true

    @javax.persistence.Transient
    @JsonIgnore
    override fun isEnabled(): Boolean = true
}

data class UserRegisterDto(val username: String, val password: String)
data class UserLoginDto(val username: String, val password: String)