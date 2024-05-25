package com.skufbet.core.api.content.service

import com.skufbet.core.api.bet.dto.FinishResult
import com.skufbet.core.api.bet.dto.kafka.FinishTo
import com.skufbet.core.api.bet.service.BetService
import com.skufbet.core.api.content.dao.LineDao
import com.skufbet.core.api.content.dao.ResultDao
import com.skufbet.core.api.graphql.model.content.Line
import com.skufbet.core.api.graphql.model.content.Result
import org.apache.kafka.clients.admin.NewTopic
import org.springframework.kafka.core.KafkaTemplate
import org.springframework.stereotype.Service
import java.util.concurrent.CompletableFuture

@Service
class LineService(
    private val lineDao: LineDao,
    private val resultDao: ResultDao,
    private val betService: BetService,
    private val kafkaTemplate: KafkaTemplate<String, FinishTo>,
    private val topic: NewTopic
) {
    fun getLinesByEventId(eventId: Int): List<Line> = lineDao.getLinesByEventId(eventId)

    fun getLineResult(resultId: Int?): Result? {
        return resultId?.let { resultDao.getResultById(it) }
    }

    fun finish(resultId: Int): FinishResult {
        val result: Result = resultDao.getResultById(resultId)
            ?: throw IllegalStateException("No result found with id: $resultId")


        val line: Line = lineDao.selectBy(result.lineId)
            ?: throw IllegalStateException("No line found with id: ${result.lineId}")

        val finishTo = FinishTo(line.id, result.id)

        kafkaTemplate.send(topic.name(), finishTo)
            .whenComplete { res, ex ->
                if (ex != null) {
                    println("KARAMBA: ${res.producerRecord.value()}")
                } else {
                    println("Sent message: ${res.producerRecord.value()}")
                }
            }
        return FinishResult(line.id, result.id)
    }

    fun updateResult(lineId: Int, resultId: Int) {
        lineDao.updateResult(lineId, resultId)
    }

    fun getAvailableResults(lines: List<Line>): Map<Line, List<Result>> {
        val results = resultDao.getAvailableResultsForLine(lines.map { it.id })
        return lines.associateWith { line ->
            results.filter { it.lineId == line.id }
        }
    }
}
