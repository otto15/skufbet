package com.skufbet.userprofile.kafka.dto

data class BalanceOperationMessage(
    val clientId: Int,
    val amount: Int,
)
