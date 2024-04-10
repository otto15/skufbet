package com.skufbet.userprofile.domain

data class UserProfile(
    val id: Int,
    val mail: String,
    val phoneNumber: String,
    val password: String,
    val balance: Int,
)