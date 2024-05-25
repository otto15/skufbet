package com.skufbet.balance.client.userprofile

import org.springframework.beans.factory.annotation.Value
import org.springframework.http.HttpEntity
import org.springframework.http.HttpMethod
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Service
import org.springframework.web.client.HttpClientErrorException
import org.springframework.web.client.RestTemplate

@Service
class UserProfileClient(
    private val restTemplate: RestTemplate,
    @Value("\${user.profile.url}")
    private val url: String,
) {
    fun withdrawFromBalance(userProfileId: Int, updateUserBalanceRequest: UpdateUserBalanceRequestTo) {
        try {
            restTemplate.exchange(
                "$url/user-profiles/${userProfileId}/balance:withdraw",
                HttpMethod.POST,
                HttpEntity(updateUserBalanceRequest),
                Void::class.java
            )
        } catch (e: HttpClientErrorException) {
            if (e.statusCode.isSameCodeAs(HttpStatus.CONFLICT)) {
                throw ConflictBalanceException("Amount should be less than balance")
            }

            if (e.statusCode.isSameCodeAs(HttpStatus.NOT_FOUND)) {
                throw NotFoundException("User profile not found")
            }
        }
    }

    fun depositToBalance(userProfileId: Int, updateUserBalanceRequest: UpdateUserBalanceRequestTo) {
        try {
            restTemplate.exchange(
                "$url/user-profiles/${userProfileId}/balance:deposit",
                HttpMethod.POST,
                HttpEntity(updateUserBalanceRequest),
                Void::class.java
            )
        } catch (e: HttpClientErrorException) {
            if (e.statusCode.isSameCodeAs(HttpStatus.NOT_FOUND)) {
                throw NotFoundException("User profile not found")
            }
        }
    }
}

data class UpdateUserBalanceRequestTo(val amount: Int)

class ConflictBalanceException(message: String) : RuntimeException(message)
class NotFoundException(message: String) : RuntimeException(message)
