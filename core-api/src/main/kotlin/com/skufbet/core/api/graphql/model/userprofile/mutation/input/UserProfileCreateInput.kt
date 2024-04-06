package com.skufbet.core.api.graphql.model.userprofile.mutation.input

import java.util.Date

data class UserProfileCreateInput(
    val mail: String,
    val phoneNumber: String,
    val password: String,
    val firstName: String,
    val lastName: String,
    val passport: String,
    val dateOfBirth: Date,
    val taxPayerId: String,
)
