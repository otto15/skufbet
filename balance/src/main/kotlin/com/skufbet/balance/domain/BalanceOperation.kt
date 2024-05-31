package com.skufbet.balance.domain

import java.time.Instant
import java.util.*

data class BalanceOperation(
    val id: UUID,
    val paymentToken: String?,
    val userProfileId: Int,
    val amount: Int,
    val type: BalanceOperationType,
    val status: Status,
    val createdDate: Instant
) {
    enum class Status {
        NOT_STARTED,
        STARTED,
        PROCESSED,
        CLEARED,
        CLEARING_FAILED,
        FAILED
    }
}
