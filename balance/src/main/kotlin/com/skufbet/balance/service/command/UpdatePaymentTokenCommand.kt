package com.skufbet.balance.service.command

import com.skufbet.balance.dto.CallbackRequestTo

data class UpdatePaymentTokenCommand(
    val paymentToken: String,
    val status: CallbackRequestTo.Status,
)
