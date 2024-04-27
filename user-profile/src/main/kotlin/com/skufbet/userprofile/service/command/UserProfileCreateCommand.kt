package com.skufbet.userprofile.service.command

import java.util.*

data class UserProfileCreateCommand(
    val keycloakId: String,
    val mail: String,
    val phoneNumber: String,
    val password: String,
    val firstName: String,
    val lastName: String,
    val passport: String,
    val dateOfBirth: Date,
    val taxPayerId: String,
)
