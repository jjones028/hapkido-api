package io.spektacle.db.tables

import org.jetbrains.exposed.sql.Column
import org.jetbrains.exposed.sql.Table

object KeyPairs : Table() {
    val id: Column<Long> = long("id").autoIncrement()
    val privateKey: Column<String> = varchar("private_key", 4096)
    val publicKey: Column<String> = varchar("public_key", 1024)
    override val primaryKey: PrimaryKey = PrimaryKey(id, name = "pk_primary_key")
}
