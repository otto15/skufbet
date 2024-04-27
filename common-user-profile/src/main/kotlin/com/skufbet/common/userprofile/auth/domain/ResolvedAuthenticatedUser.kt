package com.skufbet.common.userprofile.auth.domain

import com.skufbet.common.userprofile.domain.UserProfilePermission

@Target(AnnotationTarget.VALUE_PARAMETER)
@Retention(AnnotationRetention.RUNTIME)
@MustBeDocumented
annotation class ResolvedAuthenticatedUser(
    val permissions: Array<UserProfilePermission> = []
)

//1) написать ручку чтобы по айдишника кейклока получать профиль
//2)

