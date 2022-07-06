package com.github.nort3x.backendchallenge2.repo

import com.github.nort3x.backendchallenge2.model.MazeUser
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface UserRepo: JpaRepository<MazeUser,String> {
}