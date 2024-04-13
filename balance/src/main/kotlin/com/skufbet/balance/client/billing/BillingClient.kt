package com.skufbet.balance.client.billing

import com.skufbet.balance.client.billing.dto.PaymentCreationRequest
import com.skufbet.balance.client.billing.dto.PaymentCreationResponse
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service
import org.springframework.web.client.RestTemplate

@Service
class BillingClient(
    private val restTemplate: RestTemplate,
    @Value("\${billing.url}")
    private val url: String,
) {
    fun createPayment(paymentCreationRequest: PaymentCreationRequest): PaymentCreationResponse =
        restTemplate.postForObject(
            "$url/v1/payments",
            paymentCreationRequest,
            PaymentCreationResponse::class.java
        ) ?: throw IllegalStateException("Request to billing failed")
}
