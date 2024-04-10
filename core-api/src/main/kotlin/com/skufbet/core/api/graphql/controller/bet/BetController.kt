package com.skufbet.core.api.graphql.controller.bet

import com.skufbet.core.api.clients.userprofile.UserProfileApiClient
import com.skufbet.core.api.event.service.BetService
import com.skufbet.core.api.graphql.model.bet.mutation.BetMutation
import com.skufbet.core.api.graphql.model.bet.mutation.input.BetCreateInput
import com.skufbet.core.api.graphql.model.bet.mutation.payload.BetCreatePayload
import org.springframework.graphql.data.method.annotation.Argument
import org.springframework.graphql.data.method.annotation.MutationMapping
import org.springframework.graphql.data.method.annotation.SchemaMapping
import org.springframework.stereotype.Controller
import java.util.concurrent.CompletableFuture

@Controller
class BetController(
    private val userProfileApiClient: UserProfileApiClient,
    private val betService: BetService
) {
    @MutationMapping
    fun betMutation() = BetMutation()

    @SchemaMapping("createBet")
    fun createBet(
        betMutation: BetMutation,
        @Argument("betCreateInput") betCreateInput: BetCreateInput
    ): BetCreatePayload {

        return BetCreatePayload(1)
    }



}