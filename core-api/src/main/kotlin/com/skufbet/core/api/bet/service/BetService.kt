package com.skufbet.core.api.bet.service

import com.skufbet.core.api.bet.dao.BetDao
import com.skufbet.core.api.bet.dto.UpdateUserBalanceRequestTo
import com.skufbet.core.api.bet.service.command.BetCreateCommand
import com.skufbet.core.api.client.userprofile.UserProfileApiClient
import com.skufbet.core.api.content.dao.ResultDao
import com.skufbet.core.api.graphql.model.content.Bet
import com.skufbet.core.api.graphql.model.content.BetStatus
import com.skufbet.core.api.kafka.MessageProducer
import com.skufbet.core.api.kafka.dto.BalanceOperationMessage
import com.skufbet.utils.database.id.IdGenerator
import org.springframework.stereotype.Service
import kotlin.math.floor

@Service
class BetService(
    private val betIdGenerator: IdGenerator<Int>,
    private val betDao: BetDao,
    private val resultDao: ResultDao,
    private val userProfileApiClient: UserProfileApiClient,
    private val messageProducer: MessageProducer,
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

    fun finish(lineId: Int, resultId: Int) = betDao.selectBy(lineId)
        .asSequence()
        .filter { it.status == "ACCEPTED" }
        .map {
            betDao.changeStatus(it.id, BetStatus.CALCULATED.name)
            it
        }.filter { it.resultId == resultId }
        .forEach {
            messageProducer.sendMessage(message = BalanceOperationMessage(it.userId, floor(it.amount * it.coefficient).toInt()))
        }


    private fun coefficientFail(bet: Bet) {
        betDao.changeStatus(bet.id, BetStatus.FAILED_BY_COEFFICIENT.name)
        userProfileApiClient.depositToUserBalance(
            UpdateUserBalanceRequestTo(bet.userId, bet.amount)
        )
    }

    private fun accept(betId: Int) {
        betDao.changeStatus(betId, BetStatus.ACCEPTED.name)
    }

    fun validateCoefficient(bet: Bet) {
        val relevantCoefficient = resultDao.getResultById(bet.resultId)?.coefficient
        if (relevantCoefficient != bet.coefficient) coefficientFail(bet)
        else {
            try {
                val retrievedBet = betDao.getBy(bet.id)
                if (retrievedBet!!.status == BetStatus.VALIDATING.name) accept(retrievedBet.id)
            } catch (e: Exception) {
                throw RuntimeException("No bet found with id: ${bet.id}")
            }
        }
    }

    fun get(betId: Int): Bet? = betDao.getBy(betId)

}
