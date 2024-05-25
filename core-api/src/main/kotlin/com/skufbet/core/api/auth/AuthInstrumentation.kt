package com.skufbet.core.api.auth

import com.skufbet.common.userprofile.auth.domain.AnonymousUser
import com.skufbet.common.userprofile.auth.domain.AuthenticatedUser
import com.skufbet.common.userprofile.auth.domain.UnregisteredUser
import com.skufbet.common.userprofile.auth.domain.User
import com.skufbet.core.api.client.userprofile.UserProfileApiClient
import graphql.ExecutionResult
import graphql.execution.instrumentation.InstrumentationContext
import graphql.execution.instrumentation.InstrumentationState
import graphql.execution.instrumentation.SimplePerformantInstrumentation
import graphql.execution.instrumentation.parameters.InstrumentationExecutionParameters
import org.slf4j.LoggerFactory
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.oauth2.jwt.Jwt
import org.springframework.web.client.HttpClientErrorException.NotFound

class AuthInstrumentation(private val userProfileApiClient: UserProfileApiClient) : SimplePerformantInstrumentation() {
    override fun beginExecution(
        parameters: InstrumentationExecutionParameters,
        state: InstrumentationState?
    ): InstrumentationContext<ExecutionResult>? {
        log.info("Starting auth...")
        val user: User = if (ANONYMOUS_USER == SecurityContextHolder.getContext().authentication.principal) {
            AnonymousUser
        } else {
            (SecurityContextHolder.getContext().authentication.principal as Jwt).subject.let { keycloakId ->
                try {
                    userProfileApiClient.getUserProfileByKeycloakId(keycloakId).let { userProfileResponse ->
                        userProfileResponse?.let {
                            AuthenticatedUser(it.id, it.keycloakId, it.role.permissions)
                        }
                    } ?: throw IllegalStateException("Response from user-profile is null")
                } catch (e: NotFound) {
                    UnregisteredUser(keycloakId)
                }
            }
        }

        parameters.graphQLContext.put("Authorization", user)
        return super.beginExecution(parameters, state)
    }

    companion object {
        private val log = LoggerFactory.getLogger(AuthInstrumentation::class.java)
        private const val ANONYMOUS_USER = "anonymousUser"
    }
}
