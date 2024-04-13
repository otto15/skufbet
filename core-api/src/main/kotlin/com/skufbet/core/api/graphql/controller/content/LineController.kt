package com.skufbet.core.api.graphql.controller.content

import com.skufbet.core.api.content.service.LineService
import com.skufbet.core.api.graphql.model.content.Line
import org.springframework.graphql.data.method.annotation.Argument
import org.springframework.graphql.data.method.annotation.QueryMapping
import org.springframework.stereotype.Controller

@Controller
class LineController(
    private val lineService: LineService
) {
    @QueryMapping
    fun linesByEventId(@Argument eventId: Int) : List<Line> {
        return lineService.getLinesByEventId(eventId)
    }
}
