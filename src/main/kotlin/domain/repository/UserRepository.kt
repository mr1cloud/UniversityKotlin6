package org.example.domain.repository

import org.example.domain.model.User

interface UserRepository {
    fun findByUsername(username: String): User?
}