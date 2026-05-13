package org.example.domain.repository

import org.example.domain.model.User

interface UserRepository {
    suspend fun findByUsername(username: String): User?
    suspend fun findById(id: Int): User?
}