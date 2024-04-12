package io.spektacle.repositories

import io.spektacle.db.DatabaseSingleton.dbQuery
import io.spektacle.db.tables.SubjectNames
import io.spektacle.models.SubjectName
import org.jetbrains.exposed.sql.ResultRow
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.deleteWhere
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.update

class SubjectNameRepository : Repository<SubjectName, Long> {
    override fun toModel(row: ResultRow) = SubjectName(
        id = row[SubjectNames.id],
        commonName = row[SubjectNames.commonName],
        countryCode = row[SubjectNames.countryCode],
        organization = row[SubjectNames.organization],
        organizationalUnit = row[SubjectNames.organizationalUnit],
        statOrProvidence = row[SubjectNames.stateOrProvidence],
        locality = row[SubjectNames.locality],
        emailAddress = row[SubjectNames.emailAddress]
    )

    override suspend fun findAll() = dbQuery {
        SubjectNames.selectAll().map(::toModel)
    }

    override suspend fun delete(id: Long) = dbQuery {
        SubjectNames.deleteWhere { SubjectNames.id eq id } == 1
    }

    override suspend fun update(model: SubjectName) = dbQuery {
        model.id?.let {
            SubjectNames.update {
                it[id] = model.id
                it[commonName] = model.commonName
                it[countryCode] = model.countryCode
                it[organization] = model.organization
                it[organizationalUnit] = model.organizationalUnit
                it[stateOrProvidence] = model.statOrProvidence
                it[locality] = model.locality
                it[emailAddress] = model.emailAddress
            } == 1
        } ?: false
    }

    override suspend fun create(model: SubjectName) = dbQuery {
        val insert = SubjectNames.insert {
            it[commonName] = model.commonName
            it[countryCode] = model.countryCode
            it[organization] = model.organization
            it[organizationalUnit] = model.organizationalUnit
            it[stateOrProvidence] = model.statOrProvidence
            it[locality] = model.locality
            it[emailAddress] = model.emailAddress
        }
        insert.resultedValues?.singleOrNull()?.let(::toModel)
    }

    override suspend fun findByIdOrNull(id: Long) = dbQuery {
        SubjectNames.selectAll()
            .where(SubjectNames.id eq id)
            .map(::toModel)
            .singleOrNull()
    }
}
