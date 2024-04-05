package com.skufbet.core.api.graphql.model.event

data class Event(val id: Int, val tournamentId: Int, val sportId: Int, val name: String, val isEnd: Boolean)
