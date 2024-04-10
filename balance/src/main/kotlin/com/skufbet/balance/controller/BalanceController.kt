package com.skufbet.balance.controller

import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RestController

@RestController
class BalanceController {
    @PostMapping("/balance/withdrawal")
    fun createWithdrawal() {

    }

    @PostMapping("/balance/deposit")
    fun createDeposit() {

    }
}
