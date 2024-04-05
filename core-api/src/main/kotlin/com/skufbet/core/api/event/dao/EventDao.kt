package com.skufbet.core.api.event.dao

import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate
import org.springframework.stereotype.Repository

@Repository
class EventDao(val jdbcTemplate: NamedParameterJdbcTemplate) {

}