package dev.danielqueiroz.kbooks.infra

import com.zaxxer.hikari.HikariDataSource
import org.flywaydb.core.Flyway
import javax.sql.DataSource

data class WebappConfig(
    val httpPort: Int,
    val dbUser: String,
    val dbPassword: String,
    val dbUrl: String
)

fun createDataSource(config: WebappConfig) =
    HikariDataSource().apply {
        jdbcUrl = config.dbUrl
        username = config.dbUser
        password = config.dbPassword
    }

fun createAndMigrateDataSource(config: WebappConfig) =
    createDataSource(config).also(::migrateDataSource)

fun migrateDataSource(dataSource: DataSource) {
    Flyway.configure()
        .dataSource(dataSource)
        .locations("db/migration")
        .table("flyway_schema_history")
        .load()
        .migrate()
}