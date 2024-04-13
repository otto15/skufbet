package com.skufbet.core.api.graphql.model.content

enum class BetStatus {
    VALIDATING,
    FAILED_BY_BALANCE,
    FAILED_BY_COEFFICIENT,
    ACCEPTED,
    CALCULATED
}
