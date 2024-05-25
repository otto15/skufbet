package com.skufbet.core.api.scheduling.configuration

import com.skufbet.core.api.scheduling.job.ShowTimeJob
import com.skufbet.core.api.scheduling.properties.SchedulerProperties
import org.quartz.*
import org.quartz.impl.matchers.GroupMatcher
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties
import org.springframework.boot.autoconfigure.quartz.QuartzDataSource
import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Profile
import org.springframework.jdbc.support.JdbcTransactionManager
import org.springframework.scheduling.quartz.SchedulerFactoryBean
import javax.sql.DataSource


@Configuration
@Profile("jobs")
class SchedulingConfiguration {
    @Bean
    @ConfigurationProperties("spring.datasource.scheduling")
    fun schedulingDataSourceProperties() = DataSourceProperties()

    @Bean
    @QuartzDataSource
    fun schedulingDataSource(): DataSource = schedulingDataSourceProperties()
        .initializeDataSourceBuilder()
        .build()

    @Bean
    @ConfigurationProperties("showtime.scheduler")
    fun showtimeSchedulerProperties() = SchedulerProperties()

    @Bean
    fun showTimeJobDetail(
        @Qualifier("showtimeSchedulerProperties") schedulerProperties: SchedulerProperties
    ): JobDetail = JobBuilder
        .newJob(ShowTimeJob::class.java)
        .withIdentity("showTimeJob", schedulerProperties.permanentJobsGroupName)
        .storeDurably()
        .requestRecovery(true)
        .build()

    @Bean
    fun showTimeTrigger(
        @Qualifier("showTimeJobDetail") jobDetail: JobDetail,
        @Qualifier("showtimeSchedulerProperties") schedulerProperties: SchedulerProperties
    ): Trigger = TriggerBuilder.newTrigger()
        .forJob(jobDetail)
        .withIdentity("showTimeJobTrigger", schedulerProperties.permanentJobsGroupName)
        .withSchedule(CronScheduleBuilder.cronSchedule(schedulerProperties.cron))
        .build()

    @Bean
    fun scheduler(
        triggers: List<Trigger>,
        jobDetails: List<JobDetail>,
        factory: SchedulerFactoryBean,
        @Qualifier("showtimeSchedulerProperties") schedulerProperties: SchedulerProperties
    ): Scheduler {
        factory.setWaitForJobsToCompleteOnShutdown(true)
        val scheduler = factory.scheduler
        factory.setTransactionManager(JdbcTransactionManager())
        revalidateJobs(jobDetails, scheduler, schedulerProperties)
        rescheduleTriggers(triggers, scheduler)
        scheduler.start()
        return scheduler
    }

    fun rescheduleTriggers(triggers: List<Trigger>, scheduler: Scheduler) {
        triggers.forEach {
            if (!scheduler.checkExists(it.key)) {
                log.error("NOT EXISTS!")
                scheduler.scheduleJob(it)
            } else {
                log.error("EXISTS!")
                scheduler.rescheduleJob(it.key, it)
            }
        }
    }

    fun revalidateJobs(jobDetails: List<JobDetail>, scheduler: Scheduler, schedulerProperties: SchedulerProperties) {
        val jobKeys = jobDetails.map { it.key }
        scheduler.getJobKeys(GroupMatcher.jobGroupEquals(schedulerProperties.permanentJobsGroupName)).forEach {
            if (it !in jobKeys) {
                scheduler.deleteJob(it)
            }
        }
    }

    companion object {
        private val log: Logger = LoggerFactory.getLogger(SchedulingConfiguration::class.java)
    }
}
