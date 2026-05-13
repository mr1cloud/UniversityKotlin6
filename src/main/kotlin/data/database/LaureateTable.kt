package org.example.data.database

import org.jetbrains.exposed.dao.id.IntIdTable
import org.jetbrains.exposed.sql.ReferenceOption

object LaureateTable : IntIdTable("laureates") {
    val prizeId = reference("prize_id", PrizeTable, onDelete = ReferenceOption.CASCADE)
    val fullName = varchar("full_name", 255)
    val portion = varchar("portion", 10)
    val motivation = text("motivation").nullable()
    val portraitUrl = varchar("portrait_url", 500).nullable()
}