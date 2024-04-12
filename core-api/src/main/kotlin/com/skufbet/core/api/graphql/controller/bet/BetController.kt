package com.skufbet.core.api.graphql.controller.bet

import com.skufbet.core.api.clients.userprofile.UserProfileApiClient
import com.skufbet.core.api.event.dto.GetUserProfileTo
import com.skufbet.core.api.event.dto.UpdateUserBalanceRequestTo
import com.skufbet.core.api.event.service.BetService
import com.skufbet.core.api.event.service.command.BetCreateCommand
import com.skufbet.core.api.graphql.model.bet.mutation.BetMutation
import com.skufbet.core.api.graphql.model.bet.mutation.input.BetCreateInput
import com.skufbet.core.api.graphql.model.bet.mutation.payload.BetCreatePayload
import com.skufbet.core.api.graphql.model.content.Bet
import com.skufbet.core.api.graphql.model.content.BetStatus
import org.springframework.graphql.data.method.annotation.Argument
import org.springframework.graphql.data.method.annotation.MutationMapping
import org.springframework.graphql.data.method.annotation.SchemaMapping
import org.springframework.stereotype.Controller
import java.lang.RuntimeException
import java.util.concurrent.CompletableFuture
import java.util.concurrent.ScheduledThreadPoolExecutor
import java.util.concurrent.TimeUnit

@Controller
class BetController(
    private val userProfileApiClient: UserProfileApiClient,
    private val betService: BetService,
    private val coefficientValidatingScheduledPool: ScheduledThreadPoolExecutor
) {
    @MutationMapping
    fun betMutation() = BetMutation()

    @SchemaMapping("create")
    fun create(
        betMutation: BetMutation,
        @Argument("betCreateInput") betCreateInput: BetCreateInput
    ): BetCreatePayload {
        val bet = betService.create(betCreateInput.toCommand())
        CompletableFuture.supplyAsync {
            userProfileApiClient.getUserProfile(bet.toGetUserProfileTo())
        }.thenApplyAsync {
            it?.let {
                try {
                    userProfileApiClient.updateUserBalance(bet.toUpdateUserBalanceTo())
                } catch (e: RuntimeException) {
                    betService.balanceFail(bet.id)
                }
            } ?: betService.balanceFail(bet.id)
        }
        coefficientValidatingScheduledPool.schedule(
            { betService.validateCoefficient(bet.id, bet.resultId, bet.coefficient) },
            TIME_TO_VALIDATE_COEFFICIENT,
            TimeUnit.SECONDS
        )
        return BetCreatePayload(bet.id)
    }

    fun BetCreateInput.toCommand() = BetCreateCommand(
        this.userId,
        this.lineId,
        this.resultId,
        this.amount,
        this.coefficient,
        BetStatus.VALIDATING.name
    )

    fun Bet.toGetUserProfileTo() = GetUserProfileTo(
        this.userId,
    )

    fun Bet.toUpdateUserBalanceTo() = UpdateUserBalanceRequestTo(
        this.userId,
        this.amount
    )

    companion object {
        private const val TIME_TO_VALIDATE_COEFFICIENT = 3L
    }
}
