package com.skufbet.core.api.userprofile.service.command

import java.util.Date

data class UserProfileCreateCommand(
    val mail: String,
    val phoneNumber: String,
    val password: String,
    val firstName: String,
    val lastName: String,
    val passport: String,
    val dateOfBirth: Date,
    val taxPayerId: String,
)
