package com.skufbet.core.api.clients.userprofile

import com.skufbet.core.api.clients.userprofile.dto.CreateUserProfileRequestTo
import com.skufbet.core.api.clients.userprofile.dto.ProfileIdTo
import com.skufbet.core.api.event.dto.GetUserProfileResponse
import com.skufbet.core.api.event.dto.GetUserProfileTo
import org.springframework.beans.factory.annotation.Value
import org.springframework.http.HttpEntity
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpMethod
import org.springframework.http.MediaType
import org.springframework.stereotype.Component
import org.springframework.web.client.RestTemplate

@Component
class UserProfileApiClient(
    private val restTemplate: RestTemplate,
    @Value("\${user.profile.api.url}")
    private val serviceUrl: String
) {
    fun createUserProfile(createUserProfileRequest: CreateUserProfileRequestTo): ProfileIdTo {
        val headers = HttpHeaders().apply {
            contentType = MediaType.APPLICATION_JSON
        }

        val entity = HttpEntity(createUserProfileRequest, headers)
        return restTemplate.postForObject("$serviceUrl/user-profiles", entity, ProfileIdTo::class.java)
            ?: throw IllegalStateException("Failed to create user profile")
    }

    fun getUserProfile(getUserProfileRequest: GetUserProfileTo): GetUserProfileResponse? {
        return restTemplate.getForObject("$serviceUrl/user-profiles/${getUserProfileRequest.userId}", GetUserProfileResponse::class.java)
    }
}
