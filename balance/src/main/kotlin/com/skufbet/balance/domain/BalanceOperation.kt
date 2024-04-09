package com.skufbet.balance.domain

import java.util.UUID

data class BalanceOperation(
    val id: UUID,
    val paymentToken: String,
    val userProfileId: Int,
    val amount: Int,
    val type: Type,
    val status: Status,
) {
    enum class Type {
        WITHDRAWAL,
        DEPOSIT
    }

    enum class Status {
        NOT_STARTED,
        STARTED,
        PROCESSED,
        FAILED
    }
}
