package org.example.data.repository

import org.example.domain.model.User
import org.example.domain.repository.UserRepository

class UserRepositoryImpl : UserRepository {
    private val users = listOf(
        User("admin", "123", "admin"),
        User("user", "1234", "user")
    )

    override fun findByUsername(username: String): User? {
        return users.firstOrNull { it.username == username }
    }
}