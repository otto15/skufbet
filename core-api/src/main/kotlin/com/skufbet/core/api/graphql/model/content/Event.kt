package com.skufbet.core.api.graphql.model.content

data class Event(val id: Int, val tournamentId: Int?, val sport: String, val name: String, val isEnd: Boolean)
