package com.skufbet.core.api.graphql.controller.bet

import com.skufbet.common.userprofile.auth.domain.AuthenticatedUser
import com.skufbet.common.userprofile.domain.UserProfilePermission
import com.skufbet.core.api.auth.AuthHelper
import com.skufbet.core.api.bet.dto.GetUserProfileTo
import com.skufbet.core.api.bet.dto.UpdateUserBalanceRequestTo
import com.skufbet.core.api.bet.service.BetService
import com.skufbet.core.api.bet.service.command.BetCreateCommand
import com.skufbet.core.api.client.userprofile.UserProfileApiClient
import com.skufbet.core.api.graphql.model.bet.mutation.BetMutation
import com.skufbet.core.api.graphql.model.bet.mutation.input.BetCreateInput
import com.skufbet.core.api.graphql.model.bet.mutation.payload.BetCreatePayload
import com.skufbet.core.api.graphql.model.content.Bet
import com.skufbet.core.api.graphql.model.content.BetStatus
import graphql.schema.DataFetchingEnvironment
import org.springframework.graphql.data.method.annotation.Argument
import org.springframework.graphql.data.method.annotation.MutationMapping
import org.springframework.graphql.data.method.annotation.QueryMapping
import org.springframework.graphql.data.method.annotation.SchemaMapping
import org.springframework.stereotype.Controller
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
        env: DataFetchingEnvironment,
        betMutation: BetMutation,
        @Argument("betCreateInput") betCreateInput: BetCreateInput
    ): BetCreatePayload {
        val authenticatedUser: AuthenticatedUser = AuthHelper.checkAndGetAuthenticatedUser(
            env,
            setOf(UserProfilePermission.MAKE_BET)
        )

        val bet = betService.create(betCreateInput.toCommand(authenticatedUser.id))
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
            { betService.validateCoefficient(bet) },
            TIME_TO_VALIDATE_COEFFICIENT,
            TimeUnit.SECONDS
        )
        return BetCreatePayload(bet.id)
    }

    @QueryMapping
    fun betById(env: DataFetchingEnvironment, @Argument betId: Int): Bet? {
        AuthHelper.checkAuthenticatedUser(env, setOf(UserProfilePermission.EXPLORE_BET))

        return betService.get(betId)
    }

    fun BetCreateInput.toCommand(userId: Int) = BetCreateCommand(
        userId,
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
