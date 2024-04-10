package com.skufbet.balance.dao

import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate

class BalanceOperationDao(private val jdbcTemplate: NamedParameterJdbcTemplate) {
}
