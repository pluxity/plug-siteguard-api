package com.pluxity.siteguard.authentication.repository

import com.pluxity.siteguard.authentication.entity.RefreshToken
import org.springframework.data.repository.CrudRepository

interface RefreshTokenRepository : CrudRepository<RefreshToken, String> {
    fun findByToken(token: String): RefreshToken?
}
