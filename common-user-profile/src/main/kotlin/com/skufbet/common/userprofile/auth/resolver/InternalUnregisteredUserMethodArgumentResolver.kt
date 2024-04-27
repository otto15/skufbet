package com.skufbet.common.userprofile.auth.resolver

import com.skufbet.common.userprofile.auth.InternalHeaderNames
import com.skufbet.common.userprofile.auth.domain.UnregisteredUser
import com.skufbet.common.userprofile.auth.exception.UnauthorizedException
import org.springframework.core.MethodParameter
import org.springframework.http.HttpHeaders
import org.springframework.web.bind.support.WebDataBinderFactory
import org.springframework.web.context.request.NativeWebRequest
import org.springframework.web.method.support.HandlerMethodArgumentResolver
import org.springframework.web.method.support.ModelAndViewContainer

class InternalUnregisteredUserMethodArgumentResolver : HandlerMethodArgumentResolver {
    override fun supportsParameter(parameter: MethodParameter): Boolean {
        return parameter.parameterType == UnregisteredUser::class.java
    }

    override fun resolveArgument(
        parameter: MethodParameter,
        mavContainer: ModelAndViewContainer?,
        webRequest: NativeWebRequest,
        binderFactory: WebDataBinderFactory?
    ): Any {
        val headers: HttpHeaders = extractHttpHeaders(webRequest)

        val keycloakId: String = headers.getFirst(InternalHeaderNames.KEYCLOAK_ID)
            ?: throw UnauthorizedException("Could not resolve unregistered user")

        return UnregisteredUser(keycloakId)
    }

    private fun extractHttpHeaders(request: NativeWebRequest): HttpHeaders {
        val httpHeaders = HttpHeaders()
        val headerNames: Iterator<String> = request.headerNames
        while (headerNames.hasNext()) {
            val headerName: String = headerNames.next()
            val headerValue: String? = request.getHeader(headerName)
            httpHeaders.add(headerName, headerValue)
        }
        return httpHeaders
    }
}
