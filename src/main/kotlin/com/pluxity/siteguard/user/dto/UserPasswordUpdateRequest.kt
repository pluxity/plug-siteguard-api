package com.pluxity.siteguard.user.dto

data class UserPasswordUpdateRequest(
    val currentPassword: String,
    val newPassword: String,
)
