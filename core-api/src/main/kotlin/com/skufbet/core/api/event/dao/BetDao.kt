package com.skufbet.core.api.event.dao

import com.skufbet.core.api.graphql.model.bet.mutation.input.BetCreateInput
import org.springframework.stereotype.Repository

@Repository
class BetDao {

    fun createBet(betCreateInput: BetCreateInput) {

    }

    companion object {
        private val createBetQuery = """
            
        """.trimIndent()
    }
}
