package org.example.data.database

import org.jetbrains.exposed.sql.ReferenceOption
import org.jetbrains.exposed.sql.Table
import org.jetbrains.exposed.sql.javatime.CurrentDateTime
import org.jetbrains.exposed.sql.javatime.datetime

object UserPrizeTable : Table("user_prizes") {
    val userId = reference("user_id", UserTable, onDelete = ReferenceOption.CASCADE)
    val prizeId = reference("prize_id", PrizeTable, onDelete = ReferenceOption.CASCADE)
    val addedAt = datetime("added_at").defaultExpression(CurrentDateTime)
    override val primaryKey = PrimaryKey(userId, prizeId)
}