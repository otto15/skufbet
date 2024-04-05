package com.skufbet.core.api.event.service

import com.skufbet.core.api.event.dao.LineDao
import com.skufbet.core.api.event.dao.ResultDao
import com.skufbet.core.api.graphql.model.content.Line
import com.skufbet.core.api.graphql.model.content.Result
import org.springframework.stereotype.Service

@Service
class LineService(
    private val lineDao: LineDao,
    private val resultDao: ResultDao
) {
    fun getLinesByEventId(eventId: Int): List<Line> = lineDao.getLinesByEventId(eventId)

    fun getLineResult(resultId: Int?): Result? {
        return resultId?.let { resultDao.getResultById(it) }
    }

    fun getAvailableResults(lines: List<Line>): Map<Line, List<Result>> {
        val results = resultDao.getAvailableResultsForLine(lines.map { it.id })
        return lines.associateWith { line ->
            results.filter { it.lineId == line.id }
        }
    }
}
