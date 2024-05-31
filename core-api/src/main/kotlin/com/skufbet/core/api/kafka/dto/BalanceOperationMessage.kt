package com.skufbet.core.api.kafka.dto

data class BalanceOperationMessage(
    val clientId: Int,
    val amount: Int,
)
