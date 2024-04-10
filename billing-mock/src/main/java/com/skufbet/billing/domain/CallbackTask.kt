package com.skufbet.billing.domain

data class CallbackTask(
    val request: Request,
    val url: String,
) {
    data class Request(
        val paymentToken: String,
        val status: Status,
    )

    enum class Status {
        SUCCESS,
        FAILED,
    }
}
