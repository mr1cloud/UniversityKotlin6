package org.example.data.repository

import org.example.data.database.PrizeTable
import org.example.data.database.UserPrizeTable
import org.example.domain.model.NobelPrize
import org.example.domain.repository.FavoritesRepository
import org.jetbrains.exposed.sql.SortOrder
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.and
import org.jetbrains.exposed.sql.deleteWhere
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.transactions.experimental.newSuspendedTransaction

class FavoritesRepositoryImpl : FavoritesRepository {
    override suspend fun getFavorites(userId: Int): List<NobelPrize> =
        newSuspendedTransaction {
            (UserPrizeTable innerJoin PrizeTable)
                .selectAll()
                .where { UserPrizeTable.userId eq userId }
                .orderBy(UserPrizeTable.addedAt to SortOrder.DESC)
                .map {
                    NobelPrize(
                        id = it[PrizeTable.id].value,
                        awardYear = it[PrizeTable.awardYear],
                        category = it[PrizeTable.category],
                        fullName = it[PrizeTable.fullName],
                        motivation = it[PrizeTable.motivation],
                        detailLink = it[PrizeTable.detailLink],
                        laureates = emptyList()
                    )
                }
        }

    override suspend fun addFavorite(userId: Int, prizeId: Int): Boolean =
        newSuspendedTransaction {
            val exists = UserPrizeTable
                .selectAll()
                .where {
                    (UserPrizeTable.userId eq userId) and
                            (UserPrizeTable.prizeId eq prizeId)
                }
                .any()

            if (exists) return@newSuspendedTransaction false

            UserPrizeTable.insert {
                it[UserPrizeTable.userId] = userId
                it[UserPrizeTable.prizeId] = prizeId
            }
            true
        }

    override suspend fun removeFavorite(userId: Int, prizeId: Int): Boolean =
        newSuspendedTransaction {
            val deleted = UserPrizeTable.deleteWhere {
                (UserPrizeTable.userId eq userId) and
                        (UserPrizeTable.prizeId eq prizeId)
            }
            deleted > 0
        }

    override suspend fun isFavorite(userId: Int, prizeId: Int): Boolean =
        newSuspendedTransaction {
            UserPrizeTable
                .selectAll()
                .where {
                    (UserPrizeTable.userId eq userId) and
                            (UserPrizeTable.prizeId eq prizeId)
                }
                .any()
        }
}