package com.pluxity.siteguard.cctv.client

import com.pluxity.siteguard.cctv.dto.MediaServerPathItem
import com.pluxity.siteguard.cctv.dto.MediaServerPathListResponse
import com.pluxity.siteguard.global.config.WebClientFactory
import com.pluxity.siteguard.global.constant.ErrorCode
import com.pluxity.siteguard.global.exception.CustomException
import com.pluxity.siteguard.global.properties.MediaServerProperties
import org.springframework.stereotype.Component
import org.springframework.web.reactive.function.client.WebClient
import org.springframework.web.reactive.function.client.bodyToMono

@Component
class CctvApiClient(
    webClientFactory: WebClientFactory,
    private val mediaServerProperties: MediaServerProperties,
) {
    private val client: WebClient = webClientFactory.createClient(mediaServerProperties.url)

    fun fetchPaths(): List<MediaServerPathItem> {
        if (mediaServerProperties.url.isBlank()) {
            throw CustomException(ErrorCode.MEDIA_SERVER_URL_NOT_CONFIGURED)
        }

        val response =
            client
                .get()
                .uri("/v3/paths/list")
                .retrieve()
                .bodyToMono<MediaServerPathListResponse>()
                .block()
        return response?.items ?: emptyList()
    }
}
