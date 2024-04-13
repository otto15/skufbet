package com.skufbet.core.api.client.userprofile.dto

import java.util.Date

data class CreateUserProfileRequestTo(
    val mail: String,
    val phoneNumber: String,
    val password: String,
    val firstName: String,
    val lastName: String,
    val passport: String,
    val dateOfBirth: Date,
    val taxPayerId: String,
)
