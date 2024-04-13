package com.skufbet.core.api.bet.dto

data class GetUserProfileResponse(val id: Int, val mail: String, val phoneNumber: String, val password: String, val balance: Int)
