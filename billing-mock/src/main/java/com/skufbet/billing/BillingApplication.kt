package com.skufbet.billing

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.scheduling.annotation.EnableScheduling

@SpringBootApplication
@EnableScheduling
class BillingApplication

fun main(args: Array<String>) {
    runApplication<BillingApplication>(*args)
}
