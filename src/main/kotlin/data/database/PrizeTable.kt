package org.example.data.database

import org.jetbrains.exposed.dao.id.IntIdTable

object PrizeTable : IntIdTable("prizes") {
    val awardYear = integer("award_year")
    val category = varchar("category", 100)
    val fullName = varchar("full_name", 255)
    val motivation = text("motivation").nullable()
    val detailLink = varchar("detail_link", 500).nullable()
}