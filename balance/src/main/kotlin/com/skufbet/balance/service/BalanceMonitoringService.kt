package com.skufbet.balance.service

import com.skufbet.balance.domain.BalanceOperationType
import io.micrometer.core.instrument.Counter
import io.micrometer.core.instrument.MeterRegistry
import org.springframework.stereotype.Service

@Service
class BalanceMonitoringService(
    private val meterRegistry: MeterRegistry
) {


    fun monitorProcessingSuccess(operationType: BalanceOperationType) {
        Counter.builder(SUCCESS_BALANCE_OPERATION_SENSOR_NAME)
            .tag(TYPE_TAG_NAME, operationType.name)
            .tag(STAGE_TAG_NAME, PROCESSING_STAGE_NAME)
            .register(meterRegistry)
            .increment()
    }

    fun monitorProcessingFailure(operationType: BalanceOperationType) {
        Counter.builder(FAILED_BALANCE_OPERATION_SENSOR_NAME)
            .tag(TYPE_TAG_NAME, operationType.name)
            .tag(STAGE_TAG_NAME, PROCESSING_STAGE_NAME)
            .register(meterRegistry)
            .increment()
    }

    fun monitorClearingSuccess(operationType: BalanceOperationType) {
        Counter.builder(SUCCESS_BALANCE_OPERATION_SENSOR_NAME)
            .tag(TYPE_TAG_NAME, operationType.name)
            .tag(STAGE_TAG_NAME, CLEARING_STAGE_NAME)
            .register(meterRegistry)
            .increment()
    }

    fun monitorClearingFailure(operationType: BalanceOperationType) {
        Counter.builder(FAILED_BALANCE_OPERATION_SENSOR_NAME)
            .tag(TYPE_TAG_NAME, operationType.name)
            .tag(STAGE_TAG_NAME, CLEARING_STAGE_NAME)
            .register(meterRegistry)
            .increment()
    }

    companion object {
        private const val SUCCESS_BALANCE_OPERATION_SENSOR_NAME = "success_balance_operations"
        private const val FAILED_BALANCE_OPERATION_SENSOR_NAME = "failed_balance_operations"
        private const val STAGE_TAG_NAME = "stage"
        private const val TYPE_TAG_NAME = "operation_type"
        private const val PROCESSING_STAGE_NAME = "PROCESSING"
        private const val CLEARING_STAGE_NAME = "CLEARING"
    }
}
