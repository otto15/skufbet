package com.skufbet.billing.controller

import com.skufbet.billing.domain.CallbackTask
import com.skufbet.billing.service.CallbackService
import org.apache.commons.lang3.RandomStringUtils
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController
import kotlin.random.Random

@RestController
class BillingController(
    private val callbackService: CallbackService,
) {
    @PostMapping("/v1/payments")
    fun createPayment(@RequestBody paymentCreationRequest: PaymentCreationRequest): PaymentCreationResponse {
        val paymentToken: String = RandomStringUtils.random(20, true, true)

        callbackService.addTask(
            CallbackTask(
                CallbackTask.Request(
                    paymentToken,
                    Random.nextInt(1, 100)
                        .let { if (it > 90) CallbackTask.Status.FAILED else CallbackTask.Status.SUCCESS }
                ),
                paymentCreationRequest.callbackUrl
            )
        )

        return PaymentCreationResponse(
            paymentToken,
            "https://skufbet.com/payment-form/${paymentToken}"
        )
    }

    @PostMapping("/v1/payments/{id}/clearing")
    fun clearPayment(@PathVariable id: String) = PaymentClearingResponse(
        Random.nextInt(1, 100).let { if (it > 90) "FAILED" else "SUCCESS" }
    )

    data class PaymentCreationRequest(
        val userProfileId: Int,
        val amount: Int,
        val callbackUrl: String,
    )

    data class PaymentCreationResponse(
        val paymentToken: String,
        val formUrl: String,
    )

    data class PaymentClearingResponse(
        val status: String
    )
}
