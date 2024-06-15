package com.skufbet.billing.service

import com.skufbet.billing.domain.CallbackTask
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Service
import org.springframework.web.client.RestTemplate
import java.util.*
import java.util.concurrent.ArrayBlockingQueue
import kotlin.math.min

@Service
class CallbackService(
    private val restTemplate: RestTemplate,
    private val queue: Queue<CallbackTask> = ArrayBlockingQueue(100, true)
) {
    fun addTask(task: CallbackTask) {
        queue.offer(task)
    }

    @Scheduled(cron = "*/10 * * * * *")
    fun sendCallbacks() {
        for (i in 0 until min(100, queue.size)) {
            val task: CallbackTask = queue.poll()
            try {
                log.info("Send callback url: ${task.url}, body: ${task.request}")
                restTemplate.postForLocation(task.url, task.request)
            } catch (e: Exception) {
                queue.offer(task)
            }
        }
    }

    companion object {
        private val log: Logger = LoggerFactory.getLogger(CallbackService::class.java)
    }
}
