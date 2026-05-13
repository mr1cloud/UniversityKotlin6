package org.example.domain.usecase

import org.example.domain.repository.UserRepository
import org.example.security.JwtConfig
import org.example.security.PasswordHasher

class LoginUseCase(private val userRepository: UserRepository) {
    suspend operator fun invoke(username: String, password: String): String? {
        val user = userRepository.findByUsername(username) ?: return null
        if (!PasswordHasher.verify(password, user.passwordHash)) return null
        return JwtConfig.generateToken(user)
    }
}