package com.skufbet.balance.client.billing.dto

data class PaymentCreationResponse(
    val paymentToken: String,
    val formUrl: String,
)
