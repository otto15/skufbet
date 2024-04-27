package com.skufbet.common.userprofile.domain

enum class UserProfileRole(
    val permissions: Set<UserProfilePermission>
) {
    ADMIN(setOf(UserProfilePermission.MAKE_BET, UserProfilePermission.FINISH_BET, UserProfilePermission.EXPLORE_BET)),
    CLIENT(setOf(UserProfilePermission.MAKE_BET)),
}
