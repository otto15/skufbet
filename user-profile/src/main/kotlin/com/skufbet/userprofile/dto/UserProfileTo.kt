package com.skufbet.userprofile.dto

import com.fasterxml.jackson.annotation.JsonProperty

data class UserProfileTo(
    @JsonProperty("id") val id: Int,
    @JsonProperty("mail") val mail: String,
    @JsonProperty("phone_number") val phoneNumber: Int,
    @JsonProperty("password") val password: String,
    @JsonProperty("balance") val balance: Int,
)
