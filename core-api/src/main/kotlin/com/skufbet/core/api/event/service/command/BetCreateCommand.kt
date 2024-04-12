package com.skufbet.core.api.event.service.command

data class BetCreateCommand(val userId: Int, val lineId: Int, val resultId: Int, val amount: Int, val coefficient: Double, val status: String)
