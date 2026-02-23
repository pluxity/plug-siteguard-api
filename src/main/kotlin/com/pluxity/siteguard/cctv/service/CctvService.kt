package com.pluxity.siteguard.cctv.service

import com.pluxity.siteguard.cctv.client.CctvApiClient
import com.pluxity.siteguard.cctv.dto.CctvResponse
import com.pluxity.siteguard.cctv.dto.CctvUpdateRequest
import com.pluxity.siteguard.cctv.dto.toResponse
import com.pluxity.siteguard.cctv.entity.Cctv
import com.pluxity.siteguard.cctv.repository.CctvRepository
import com.pluxity.siteguard.global.constant.ErrorCode
import com.pluxity.siteguard.global.exception.CustomException
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional(readOnly = true)
class CctvService(
    private val repository: CctvRepository,
    private val apiClient: CctvApiClient,
) {
    companion object {
        private const val MAX_FAVORITE_COUNT = 4
    }

    @Transactional
    fun sync() {
        val externalPaths = apiClient.fetchPaths()
        val externalPathNames = externalPaths.map { it.name }

        val existingCctvs = repository.findAll()
        val existingPathMap = existingCctvs.associateBy { it.path }

        // 새로운 path → Cctv 엔티티 생성 후 저장
        val newCctvs =
            externalPathNames
                .filter { it !in existingPathMap }
                .map { Cctv(path = it) }
        if (newCctvs.isNotEmpty()) {
            repository.saveAll(newCctvs)
        }

        // DB에 있지만 미디어서버에 없는 path → 삭제
        val externalPathSet = externalPathNames.toSet()
        val toDelete = existingCctvs.filter { it.path !in externalPathSet }
        if (toDelete.isNotEmpty()) {
            repository.deleteAllInBatch(toDelete)
        }
    }

    fun findAll(): List<CctvResponse> =
        repository
            .findAllByOrderByIsFavoriteDescNameAsc()
            .map { it.toResponse() }

    @Transactional
    fun update(
        id: Long,
        request: CctvUpdateRequest,
    ) {
        getById(id).update(
            name = request.name,
            lon = request.lon,
            lat = request.lat,
        )
    }

    @Transactional
    fun addFavorite(id: Long) {
        val cctv = getById(id)
        if (cctv.isFavorite) {
            throw CustomException(ErrorCode.ALREADY_FAVORITE, id)
        }
        if (repository.countByIsFavoriteTrue() >= MAX_FAVORITE_COUNT) {
            throw CustomException(ErrorCode.EXCEED_FAVORITE_LIMIT)
        }
        cctv.markFavorite()
    }

    @Transactional
    fun removeFavorite(id: Long) {
        val cctv = getById(id)
        if (!cctv.isFavorite) {
            throw CustomException(ErrorCode.NOT_FAVORITE, id)
        }
        cctv.unmarkFavorite()
    }

    private fun getById(id: Long): Cctv =
        repository.findByIdOrNull(id)
            ?: throw CustomException(ErrorCode.NOT_FOUND_CCTV, id)
}
