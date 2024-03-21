package io.spektacle.db

import com.zaxxer.hikari.HikariConfig
import com.zaxxer.hikari.HikariDataSource
import io.ktor.server.config.ApplicationConfig
import io.spektacle.db.tables.SubjectNames
import io.spektacle.db.tables.Users
import kotlinx.coroutines.Dispatchers
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.transactions.experimental.newSuspendedTransaction
import org.jetbrains.exposed.sql.transactions.transaction
import java.io.File

object DatabaseSingleton {
    fun init(config: ApplicationConfig) {
        val jdbcURL = config.property("storage.jdbcURL").getString() +
                (config.propertyOrNull("storage.dbFilePath")?.getString()?.let {
                    File(it).canonicalFile.absolutePath
                } ?: "")
        val database = Database.connect(
            createHikariDataSource(
                url = jdbcURL,
                driver = config.property("storage.driverClassName").getString(),
                username = config.propertyOrNull("storage.username")?.getString(),
                password = config.propertyOrNull("storage.password")?.getString(),
                maxPoolSize = config.propertyOrNull("storage.maxpoolsize")?.getString()?.toInt() ?: 3
            )
        )
        transaction(database) {
            SchemaUtils.create(Users)
            SchemaUtils.create(SubjectNames)
        }
    }

    private fun createHikariDataSource(
        url: String,
        driver: String,
        username: String?,
        password: String?,
        maxPoolSize: Int
    ) = HikariDataSource(HikariConfig().apply {
        driverClassName = driver
        jdbcUrl = url
        maximumPoolSize = maxPoolSize
        this.username = username
        this.password = password
        validate()
    })

    suspend fun <T> dbQuery(block: suspend () -> T): T =
        newSuspendedTransaction(Dispatchers.IO) { block() }
}
