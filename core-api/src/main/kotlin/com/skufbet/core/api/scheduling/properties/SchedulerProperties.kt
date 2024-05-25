package com.skufbet.core.api.scheduling.properties

data class SchedulerProperties(
    var permanentJobsGroupName: String = "PERMANENT",
    var cron: String = "0 0 * * * ?"
)
