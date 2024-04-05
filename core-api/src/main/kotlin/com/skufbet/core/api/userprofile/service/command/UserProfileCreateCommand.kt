package com.skufbet.core.api.userprofile.service.command

data class UserProfileCreateCommand(
    val mail: String,
    val phoneNumber: String,
    val password: String,
)
