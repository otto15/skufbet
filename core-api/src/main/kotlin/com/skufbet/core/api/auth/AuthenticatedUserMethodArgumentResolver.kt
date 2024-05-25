package com.skufbet.core.api.auth

import com.skufbet.common.userprofile.auth.domain.AuthenticatedUser
import com.skufbet.common.userprofile.auth.domain.ResolvedAuthenticatedUser
import com.skufbet.common.userprofile.auth.domain.User
import com.skufbet.common.userprofile.auth.exception.UnauthorizedException
import graphql.schema.DataFetchingEnvironment
import org.springframework.core.MethodParameter
import org.springframework.graphql.data.method.HandlerMethodArgumentResolver
import org.springframework.stereotype.Component

@Component
class AuthenticatedUserMethodArgumentResolver : HandlerMethodArgumentResolver {
    override fun supportsParameter(parameter: MethodParameter): Boolean {
        return parameter.hasParameterAnnotation(ResolvedAuthenticatedUser::class.java)
            && parameter.parameterType == AuthenticatedUser::class.java
    }

    override fun resolveArgument(parameter: MethodParameter, environment: DataFetchingEnvironment): Any {
        val user: User = environment.graphQlContext["Authorization"]

        if (user !is AuthenticatedUser) {
            throw UnauthorizedException("Authenticated user is requested")
        }

        val annotation: ResolvedAuthenticatedUser = parameter.getMethodAnnotation(ResolvedAuthenticatedUser::class.java)
            ?: throw IllegalStateException("ResolvedAuthenticatedUser annotation is required")

        if (!user.permissions.containsAll(annotation.permissions.toSet())) {
            throw UnauthorizedException("Authenticated user does not have required permissions")
        }

        return user
    }
}