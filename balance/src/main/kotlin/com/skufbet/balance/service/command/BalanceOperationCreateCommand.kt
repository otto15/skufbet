package com.skufbet.balance.service.command

data class BalanceOperationCreateCommand(
    val userProfileId: Int,
    val amount: Int,
)
