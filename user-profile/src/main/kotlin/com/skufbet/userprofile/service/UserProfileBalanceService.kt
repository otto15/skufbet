package com.skufbet.userprofile.service

import com.skufbet.common.userprofile.domain.UserProfile
import com.skufbet.userprofile.dao.UserProfileDao
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Service
import org.springframework.web.server.ResponseStatusException

@Service
class UserProfileBalanceService(val userProfileDao: UserProfileDao) {
    fun withdraw(userProfileId: Int, amount: Int) {
        if (amount <= 0) {
            throw ResponseStatusException(
                HttpStatus.BAD_REQUEST,
                "Negative amount provided"
            )
        }

        val userProfile: UserProfile = userProfileDao.selectBy(userProfileId) ?: throw ResponseStatusException(
            HttpStatus.NOT_FOUND,
            "User profile with id: $userProfileId not found"
        )

        if (userProfile.balance < amount) {
            throw ResponseStatusException(
                HttpStatus.CONFLICT,
                "Withdrawal leads to negative balance"
            )
        }

        if (!userProfileDao.updateBalanceMinus(userProfileId, amount)) {
            throw ResponseStatusException(
                HttpStatus.CONFLICT,
                "Withdrawal is rejected due to access conflict"
            )
        }
    }

    fun deposit(userProfileId: Int, amount: Int) {
        if (amount <= 0) {
            throw ResponseStatusException(
                HttpStatus.BAD_REQUEST,
                "Negative amount provided"
            )
        }

        userProfileDao.selectBy(userProfileId) ?: throw ResponseStatusException(
            HttpStatus.NOT_FOUND,
            "User profile with id: $userProfileId not found"
        )

        userProfileDao.updateBalancePlus(userProfileId, amount)
    }
}
