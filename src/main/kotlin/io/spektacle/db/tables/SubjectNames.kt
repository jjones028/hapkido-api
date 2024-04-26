package io.spektacle.db.tables

import org.jetbrains.exposed.sql.Column
import org.jetbrains.exposed.sql.Table

@Suppress("MagicNumber")
object SubjectNames : Table() {
    val id: Column<Long> = long("id").autoIncrement()
    val commonName: Column<String> = varchar("common_name", 100).uniqueIndex()
    val countryCode: Column<String> = varchar("country_code", 3)
    val organization: Column<String> = varchar("organization", 250)
    val stateOrProvince: Column<String?> = varchar("state_or_province", 100).nullable()
    val organizationalUnit: Column<String?> = varchar("organizational_unit", 100).nullable()
    val locality: Column<String?> = varchar("locality", 100).nullable()
    val emailAddress: Column<String?> = varchar("email_address", 100).nullable()
    override val primaryKey = PrimaryKey(id, name = "pk_subject_names_id")
}
