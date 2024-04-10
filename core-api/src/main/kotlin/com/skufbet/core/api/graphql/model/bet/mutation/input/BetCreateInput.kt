package com.skufbet.core.api.graphql.model.bet.mutation.input

data class BetCreateInput(val id: Int, val lineId: Int, val resultId: Int, val amount: Int, val coefficient: Double)
