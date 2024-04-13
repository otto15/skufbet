package com.skufbet.core.api.graphql.model.content

data class Bet(val id: Int, val userId: Int, val lineId: Int, val resultId: Int, val amount: Int, val coefficient: Double, val status: String)
