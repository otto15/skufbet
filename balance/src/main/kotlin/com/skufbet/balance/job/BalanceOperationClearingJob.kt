package com.skufbet.balance.job

import com.skufbet.balance.domain.BalanceOperation
import com.skufbet.balance.service.BalanceOperationService
import net.javacrumbs.shedlock.spring.annotation.SchedulerLock
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Value
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Service
import java.time.LocalDateTime
import java.time.ZoneOffset

@Service
class BalanceOperationClearingJob(
    private val balanceOperationService: BalanceOperationService,
    @Value("\${clearing.trigger.days:1}")
    private val clearingTriggerDays: Long
) {
    @Scheduled(cron = "0/10 * * * * ?")
    @SchedulerLock(name = "BalanceScheduler_balanceClearing", lockAtLeastFor = "PT5S", lockAtMostFor = "PT10M")
    fun performClearing() {
        log.info("Starting BalanceOperationClearingJob")

        val balanceOperations: List<BalanceOperation> = balanceOperationService.getAvailableForClearing(
            LocalDateTime.now().minusDays(clearingTriggerDays).toInstant(ZoneOffset.UTC)
        )

        log.info("Payments available for clearing: ${balanceOperations.map { it.id }}")

        balanceOperations.forEach {
            balanceOperationService.clear(it.id)
        }

        log.info("BalanceOperationClearingJob ending")
    }

    companion object {
        private val log: Logger = LoggerFactory.getLogger(BalanceOperationClearingJob::class.java)
    }
}