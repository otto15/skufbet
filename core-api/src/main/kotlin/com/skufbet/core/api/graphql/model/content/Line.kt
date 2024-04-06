package com.skufbet.core.api.graphql.model.content

data class Line(val id: Int, val eventId: Int, val type: String, val resultId: Int?, val isClosed: Boolean)
