package com.pluxity.siteguard.cctv.client

import com.pluxity.siteguard.cctv.dto.MediaServerPathItem
import com.pluxity.siteguard.cctv.dto.MediaServerPathListResponse
import com.pluxity.siteguard.global.config.WebClientFactory
import com.pluxity.siteguard.global.properties.MediaServerProperties
import io.github.oshai.kotlinlogging.KotlinLogging
import org.springframework.stereotype.Component
import org.springframework.web.reactive.function.client.WebClient
import org.springframework.web.reactive.function.client.bodyToMono

private val log = KotlinLogging.logger {}

@Component
class CctvApiClient(
    webClientFactory: WebClientFactory,
    private val mediaServerProperties: MediaServerProperties,
) {
    private val client: WebClient =
        webClientFactory
            .createClient(mediaServerProperties.url)
            .mutate()
            .build()

    fun fetchPaths(): List<MediaServerPathItem> {
        if (mediaServerProperties.url.isBlank()) {
            log.warn { "Media Server API URL is not configured" }
            return emptyList()
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
