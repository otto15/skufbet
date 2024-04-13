package com.skufbet.balance.controller

import com.skufbet.balance.dto.CallbackRequestTo
import com.skufbet.balance.service.BalanceOperationService
import com.skufbet.balance.service.command.BalanceOperationCreateCommand
import com.skufbet.balance.service.command.UpdatePaymentTokenCommand
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController

@RestController
class BalanceOperationController(
    private val balanceOperationService: BalanceOperationService,
) {
    @PostMapping("/balance-operation/withdrawal")
    fun createWithdrawal(@RequestBody balanceOperationRequestTo: BalanceOperationRequestTo) =
        balanceOperationService.createWithdrawal(
            BalanceOperationCreateCommand(
                balanceOperationRequestTo.userProfileId,
                balanceOperationRequestTo.amount,
            )
        )

    @PostMapping("/balance-operation/deposit")
    fun createDeposit(@RequestBody balanceOperationRequestTo: BalanceOperationRequestTo) =
        balanceOperationService.createDeposit(
            BalanceOperationCreateCommand(
                balanceOperationRequestTo.userProfileId,
                balanceOperationRequestTo.amount,
            )
        )

    @PostMapping("/v1/payment-callback")
    fun processCallback(@RequestBody callbackRequestTo: CallbackRequestTo) {
        balanceOperationService.updatePaymentToken(
            UpdatePaymentTokenCommand(callbackRequestTo.paymentToken, callbackRequestTo.status)
        )
    }

    data class BalanceOperationRequestTo(val userProfileId: Int, val amount: Int)
}
