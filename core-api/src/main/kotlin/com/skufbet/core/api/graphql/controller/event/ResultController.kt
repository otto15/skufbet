package com.skufbet.core.api.graphql.controller.event

import com.skufbet.core.api.event.service.LineService
import com.skufbet.core.api.graphql.model.content.Line
import com.skufbet.core.api.graphql.model.content.Result
import org.springframework.graphql.data.method.annotation.BatchMapping
import org.springframework.graphql.data.method.annotation.SchemaMapping
import org.springframework.stereotype.Controller

@Controller
class ResultController(
    private val lineService: LineService
) {
    @BatchMapping(typeName = "Line", field = "availableResults")
    fun getAvailableResults(lines: List<Line>) : Map<Line, List<Result>> = lineService.getAvailableResults(lines)

    @SchemaMapping(typeName = "Line", field = "lineResult")
    fun getLineResult(line: Line) : Result? = lineService.getLineResult(line.resultId)
}
