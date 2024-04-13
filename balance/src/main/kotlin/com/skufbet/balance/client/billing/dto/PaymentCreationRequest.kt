package com.skufbet.balance.client.billing.dto

data class PaymentCreationRequest(
    val userProfileId: Int,
    val amount: Int,
    val callbackUrl: String,
)
