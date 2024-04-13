package com.skufbet.core.api.bet.service

import com.skufbet.core.api.bet.dao.BetDao
import com.skufbet.core.api.content.dao.ResultDao
import com.skufbet.core.api.bet.service.command.BetCreateCommand
import com.skufbet.core.api.graphql.model.content.Bet
import com.skufbet.core.api.graphql.model.content.BetStatus
import com.skufbet.utils.database.id.IdGenerator
import org.springframework.stereotype.Service
import java.lang.Exception
import java.lang.RuntimeException

@Service
class BetService(
    private val betIdGenerator: IdGenerator<Int>,
    private val betDao: BetDao,
    private val resultDao: ResultDao
) {
    fun create(betCreateCommand: BetCreateCommand): Bet {
        val bet = Bet(
            betIdGenerator.generate(),
            betCreateCommand.userId,
            betCreateCommand.lineId,
            betCreateCommand.resultId,
            betCreateCommand.amount,
            betCreateCommand.coefficient,
            BetStatus.VALIDATING.name
        )
        betDao.create(bet)
        return bet
    }

    fun balanceFail(betId: Int) {
        betDao.changeStatus(betId, BetStatus.FAILED_BY_BALANCE.name)
    }

    private fun coefficientFail(betId: Int) {
        betDao.changeStatus(betId, BetStatus.FAILED_BY_COEFFICIENT.name)
    }

    private fun accept(betId: Int) {
        betDao.changeStatus(betId, BetStatus.ACCEPTED.name)
    }

    fun validateCoefficient(betId: Int, resultId: Int, coefficient: Double) {
        val relevantCoefficient = resultDao.getResultById(resultId)?.coefficient
        if (relevantCoefficient != coefficient) coefficientFail(betId)
        else {
            try {
                val bet = betDao.getBy(betId)
                if (bet!!.status == BetStatus.VALIDATING.name) accept(betId)
            } catch (e: Exception) {
                throw RuntimeException("No bet found with id: $betId")
            }
        }
    }
}
