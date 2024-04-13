package com.skufbet.core.api.content.service

import com.skufbet.core.api.bet.dto.FinishResult
import com.skufbet.core.api.bet.service.BetService
import com.skufbet.core.api.content.dao.LineDao
import com.skufbet.core.api.content.dao.ResultDao
import com.skufbet.core.api.graphql.model.content.Line
import com.skufbet.core.api.graphql.model.content.Result
import org.springframework.stereotype.Service
import java.util.concurrent.CompletableFuture

@Service
class LineService(
    private val lineDao: LineDao,
    private val resultDao: ResultDao,
    private val betService: BetService,
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

        lineDao.updateResult(line.id, result.id)

        CompletableFuture.runAsync {
            betService.finish(line.id, result.id)
        }

        return FinishResult(line.id, result.id)
    }

    fun getAvailableResults(lines: List<Line>): Map<Line, List<Result>> {
        val results = resultDao.getAvailableResultsForLine(lines.map { it.id })
        return lines.associateWith { line ->
            results.filter { it.lineId == line.id }
        }
    }
}
