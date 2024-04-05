package com.skufbet.skufdb.id

import org.springframework.jdbc.core.namedparam.MapSqlParameterSource
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate

class PgSequenceIdGenerator(
    private val sequenceName: String,
    private val jdbcTemplate: NamedParameterJdbcTemplate,
) : IdGenerator<Int> {
    override fun generate(): Int = jdbcTemplate.queryForObject(
        GENERATE_SEQUENCE_QUERY_TEMPLATE.format(sequenceName),
        MapSqlParameterSource(),
        Int::class.java
    ) ?: throw RuntimeException("Sequence generation failed: $sequenceName")

    companion object {
        private const val GENERATE_SEQUENCE_QUERY_TEMPLATE = "SELECT nextval('%s');"
    }
}
