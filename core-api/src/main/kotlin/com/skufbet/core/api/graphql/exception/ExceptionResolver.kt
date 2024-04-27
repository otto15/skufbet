package com.skufbet.core.api.graphql.exception

import com.skufbet.common.userprofile.auth.exception.UnauthorizedException
import graphql.GraphQLError
import graphql.GraphqlErrorBuilder
import graphql.schema.DataFetchingEnvironment
import org.springframework.graphql.execution.DataFetcherExceptionResolverAdapter
import org.springframework.graphql.execution.ErrorType
import org.springframework.stereotype.Component


@Component
class ExceptionResolver : DataFetcherExceptionResolverAdapter() {
    override fun resolveToSingleError(ex: Throwable, env: DataFetchingEnvironment): GraphQLError? {
        return if (ex is UnauthorizedException) {
            GraphqlErrorBuilder.newError()
                .errorType(ErrorType.UNAUTHORIZED)
                .message(ex.message)
                .path(env.executionStepInfo.path)
                .location(env.field.sourceLocation)
                .build()
        } else {
            null
        }
    }
}