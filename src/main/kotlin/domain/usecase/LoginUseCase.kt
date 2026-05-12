package org.example.domain.usecase

import org.example.domain.repository.UserRepository
import org.example.security.JwtConfig

class LoginUseCase(private val userRepository: UserRepository) {
    operator fun invoke(username: String, password: String): String? {
        val user = userRepository.findByUsername(username) ?: return null
        if (user.password == password) {
            return JwtConfig.generateToken(user.username, user.role)
        }
        return null
    }
}