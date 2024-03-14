package io.spektacle.tables

import org.jetbrains.exposed.sql.Column
import org.jetbrains.exposed.sql.Table

object Users : Table() {
    val id: Column<Long> = long("id").autoIncrement()
    val username: Column<String> = varchar("username",50).uniqueIndex()
    val firstName: Column<String> = varchar("first_name",100)
    val lastName: Column<String?> = varchar("last_name", 250).nullable()
    override val primaryKey = PrimaryKey(id, name = "pk_users_id")
}