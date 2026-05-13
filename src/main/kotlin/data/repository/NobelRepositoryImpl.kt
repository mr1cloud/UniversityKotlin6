package org.example.data.repository

import org.example.data.database.LaureateTable
import org.example.data.database.PrizeTable
import org.example.domain.model.Laureate
import org.example.domain.model.NobelPrize
import org.example.domain.repository.NobelRepository
import org.jetbrains.exposed.sql.Op
import org.jetbrains.exposed.sql.ResultRow
import org.jetbrains.exposed.sql.SortOrder
import org.jetbrains.exposed.sql.and
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.transactions.experimental.newSuspendedTransaction

class NobelRepositoryImpl : NobelRepository {
    override suspend fun getAllPrizes(year: Int?, category: String?): NobelPrize? =
        newSuspendedTransaction {
            val prize = PrizeTable
                .selectAll()
                .where {
                    val conditions = mutableListOf<Op<Boolean>>()
                    year?.let { conditions.add(PrizeTable.awardYear eq it) }
                    category?.let { conditions.add(PrizeTable.category eq it.lowercase()) }
                    conditions.reduceOrNull { acc, op -> acc and op } ?: Op.TRUE
                }
                .orderBy(PrizeTable.awardYear to SortOrder.DESC)
                .map { it.toPrize() }
                .firstOrNull() ?: return@newSuspendedTransaction null

            if (year == null && category == null) {
                return@newSuspendedTransaction prize
            }

            val laureates = LaureateTable
                .selectAll()
                .where { LaureateTable.prizeId eq prize.id }
                .map { it.toLaureate() }

            prize.copy(laureates = laureates)
        }

    override suspend fun getLaureates(year: Int, category: String): List<Laureate> =
        newSuspendedTransaction {
            val prizeId = PrizeTable
                .selectAll()
                .where {
                    (PrizeTable.awardYear eq year) and
                            (PrizeTable.category eq category.lowercase())
                }
                .map { it[PrizeTable.id].value }
                .firstOrNull() ?: return@newSuspendedTransaction emptyList()

            LaureateTable
                .selectAll()
                .where { LaureateTable.prizeId eq prizeId }
                .map { it.toLaureate() }
        }

    private fun ResultRow.toPrize() = NobelPrize(
        id = this[PrizeTable.id].value,
        awardYear = this[PrizeTable.awardYear],
        category = this[PrizeTable.category],
        fullName = this[PrizeTable.fullName],
        motivation = this[PrizeTable.motivation],
        detailLink = this[PrizeTable.detailLink],
        laureates = emptyList()
    )

    private fun ResultRow.toLaureate() = Laureate(
        id = this[LaureateTable.id].value,
        prizeId = this[LaureateTable.prizeId].value,
        fullName = this[LaureateTable.fullName],
        portion = this[LaureateTable.portion],
        motivation = this[LaureateTable.motivation],
        portraitUrl = this[LaureateTable.portraitUrl]
    )
}