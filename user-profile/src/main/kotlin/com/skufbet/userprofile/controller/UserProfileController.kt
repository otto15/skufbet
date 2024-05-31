package com.skufbet.userprofile.controller

import com.skufbet.common.userprofile.domain.UserProfile
import com.skufbet.userprofile.dto.CreateUserProfileRequestTo
import com.skufbet.userprofile.dto.ProfileIdTo
import com.skufbet.userprofile.service.UserProfileBalanceService
import com.skufbet.userprofile.service.UserProfileCreationService
import com.skufbet.userprofile.service.command.UserProfileCreateCommand
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*


@RestController
class UserProfileController(
    private val userProfileCreationService: UserProfileCreationService,
    private val userProfileBalanceService: UserProfileBalanceService,
) {
    @PostMapping("/user-profiles")
    fun create(
        @RequestBody createUserProfileRequestTo: CreateUserProfileRequestTo
    ): ProfileIdTo {
        return userProfileCreationService
            .create(createUserProfileRequestTo.toCommand())
            .toDto()
    }

    @PostMapping("/user-profiles/{id}/balance:withdraw")
    fun withdrawFromBalance(@PathVariable id: Int, @RequestBody balanceOperationRequestTo: BalanceOperationRequestTo) {
        userProfileBalanceService.withdraw(id, balanceOperationRequestTo.amount)
    }

    @PostMapping("/user-profiles/{id}/balance:deposit")
    fun depositToBalance(@PathVariable id: Int, @RequestBody balanceOperationRequestTo: BalanceOperationRequestTo) {
        userProfileBalanceService.deposit(id, balanceOperationRequestTo.amount)
    }

    @GetMapping("/user-profiles/{id}")
    fun get(@PathVariable id: Int): ResponseEntity<Any> {
        val userProfile = userProfileCreationService.get(id)
        return userProfile?.let { ResponseEntity(userProfile, HttpStatus.OK) }
            ?: ResponseEntity(HttpStatus.NOT_FOUND)
    }

    @GetMapping("/user-profiles/keycloak/{id}")
    fun getKeycloakId(@PathVariable id: String): ResponseEntity<Any> {
        val userProfile = userProfileCreationService.getByKeycloakId(id)
        return userProfile?.let { ResponseEntity(userProfile, HttpStatus.OK) }
            ?: ResponseEntity(HttpStatus.NOT_FOUND)
    }

    data class BalanceOperationRequestTo(val amount: Int)

    fun CreateUserProfileRequestTo.toCommand() = UserProfileCreateCommand(
        this.keycloakId,
        this.mail,
        this.phoneNumber,
        this.password,
        this.firstName,
        this.lastName,
        this.passport,
        this.dateOfBirth,
        this.taxPayerId,
    )

    fun UserProfile.toDto() = ProfileIdTo(this.id)
}
