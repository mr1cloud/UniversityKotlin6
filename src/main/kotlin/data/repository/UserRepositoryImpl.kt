package org.example.data.repository

import org.example.data.database.UserTable
import org.example.domain.model.User
import org.example.domain.repository.UserRepository
import org.jetbrains.exposed.sql.ResultRow
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.transactions.experimental.newSuspendedTransaction

class UserRepositoryImpl : UserRepository {
    override suspend fun findByUsername(username: String): User? =
        newSuspendedTransaction {
            UserTable
                .selectAll()
                .where { UserTable.username eq username }
                .map { it.toUser() }
                .firstOrNull()
        }

    override suspend fun findById(id: Int): User? =
        newSuspendedTransaction {
            UserTable
                .selectAll()
                .where { UserTable.id eq id }
                .map { it.toUser() }
                .firstOrNull()
        }

    private fun ResultRow.toUser() = User(
        id = this[UserTable.id].value,
        username = this[UserTable.username],
        passwordHash = this[UserTable.passwordHash],
        role = this[UserTable.role]
    )
}