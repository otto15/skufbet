package com.skufbet.core.api.graphql.controller.content

import com.skufbet.common.userprofile.domain.UserProfilePermission
import com.skufbet.core.api.auth.AuthHelper
import com.skufbet.core.api.bet.dto.FinishResult
import com.skufbet.core.api.bet.dto.kafka.FinishTo
import com.skufbet.core.api.bet.service.BetService
import com.skufbet.core.api.content.service.LineService
import com.skufbet.core.api.graphql.model.content.Line
import com.skufbet.core.api.graphql.model.content.Result
import graphql.schema.DataFetchingEnvironment
import org.apache.kafka.clients.admin.NewTopic
import org.springframework.graphql.data.method.annotation.Argument
import org.springframework.graphql.data.method.annotation.BatchMapping
import org.springframework.graphql.data.method.annotation.MutationMapping
import org.springframework.graphql.data.method.annotation.SchemaMapping
import org.springframework.kafka.annotation.KafkaListener
import org.springframework.stereotype.Controller
import java.util.concurrent.CompletableFuture

@Controller
class ResultController(
    private val lineService: LineService,
    private val betService: BetService,
) {
    @BatchMapping(typeName = "Line", field = "availableResults")
    fun getAvailableResults(lines: List<Line>) : Map<Line, List<Result>> = lineService.getAvailableResults(lines)

    @SchemaMapping(typeName = "Line", field = "lineResult")
    fun getLineResult(line: Line) : Result? = lineService.getLineResult(line.resultId)

    @MutationMapping("line")
    fun lineMutation() = LineMutation()

    @SchemaMapping("finish")
    fun startFinish(
        env: DataFetchingEnvironment,
        lineMutation: LineMutation,
        @Argument("finishLineInput") finishLineInput: FinishLineInput
    ): FinishLinePayload {
        AuthHelper.checkAuthenticatedUser(env, setOf(UserProfilePermission.FINISH_BET))

        return lineService.finish(finishLineInput.resultId).toPayload()
    }

    @KafkaListener(topics = ["finish-bet"], groupId = "finish-group")
    fun endFinish(finishTo: FinishTo) {
        println("Message recieved: $finishTo")
        lineService.updateResult(finishTo.lineId, finishTo.resultId)
        CompletableFuture.runAsync {
            betService.finish(finishTo.lineId, finishTo.resultId)
        }
    }

    fun FinishResult.toPayload() = FinishLinePayload(this.lineId, this.resultId)

    class LineMutation

    data class FinishLineInput(val resultId: Int)

    data class FinishLinePayload(val lineId: Int, val resultId: Int)
}
