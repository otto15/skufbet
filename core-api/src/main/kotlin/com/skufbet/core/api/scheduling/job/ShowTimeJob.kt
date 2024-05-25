package com.skufbet.core.api.scheduling.job

import com.skufbet.core.api.scheduling.service.ShowTimeService
import org.quartz.DisallowConcurrentExecution
import org.quartz.JobExecutionContext
import org.quartz.PersistJobDataAfterExecution
import org.springframework.scheduling.quartz.QuartzJobBean
import java.time.LocalDateTime

@DisallowConcurrentExecution
@PersistJobDataAfterExecution
class ShowTimeJob(
    private val showTimeService: ShowTimeService
) : QuartzJobBean() {
    override fun executeInternal(context: JobExecutionContext) {
        val dataMap = context.jobDetail.jobDataMap
        val lastExecutionDateTime = dataMap["lastExecutionDateTime"] as LocalDateTime?
        dataMap["lastExecutionDateTime"] = showTimeService.showTime(lastExecutionDateTime)
    }
}
