package com.skufbet.balance.dto

data class CallbackRequestTo(
    val paymentToken: String,
    val status: Status,
) {
    enum class Status {
        SUCCESS,
        FAILED,
    }
}
