package com.skufbet.userprofile.dto

import com.fasterxml.jackson.annotation.JsonProperty
import java.util.Date

data class CreateUserProfileRequestTo(
    @JsonProperty("mail") val mail: String,
    @JsonProperty("phoneNumber") val phoneNumber: String,
    @JsonProperty("password") val password: String,
    @JsonProperty("firstName") val firstName: String,
    @JsonProperty("lastName") val lastName: String,
    @JsonProperty("passport") val passport: String,
    @JsonProperty("dateOfBirth") val dateOfBirth: Date,
    @JsonProperty("taxPayerId") val taxPayerId: String,
)
