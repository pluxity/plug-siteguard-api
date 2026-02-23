package com.pluxity.siteguard.attendance.client

import com.pluxity.siteguard.attendance.dto.AttendanceExternalData
import com.pluxity.siteguard.attendance.dto.AttendanceExternalResponse
import com.pluxity.siteguard.global.config.WebClientFactory
import com.pluxity.siteguard.global.properties.AttendanceProperties
import io.github.oshai.kotlinlogging.KotlinLogging
import org.springframework.stereotype.Component
import org.springframework.web.reactive.function.client.WebClient
import org.springframework.web.reactive.function.client.bodyToMono

private val log = KotlinLogging.logger {}

@Component
class AttendanceApiClient(
    webClientFactory: WebClientFactory,
    private val attendanceProperties: AttendanceProperties,
) {
    private val client: WebClient = webClientFactory.createClient(attendanceProperties.url)

    fun fetchAttendanceData(): List<AttendanceExternalData> {
        if (attendanceProperties.url.isBlank()) {
            log.warn { "Attendance API URL is not configured" }
            return emptyList()
        }

        val response =
            client
                .get()
                .retrieve()
                .bodyToMono<AttendanceExternalResponse>()
                .block()
        return response?.let { response.data } ?: emptyList()
    }
}
