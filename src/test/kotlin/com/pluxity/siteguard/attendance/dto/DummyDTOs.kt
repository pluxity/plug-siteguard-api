package com.pluxity.siteguard.attendance.dto

import com.pluxity.siteguard.attendance.entity.Attendance
import com.pluxity.siteguard.base.entity.withId
import java.time.LocalDate

fun dummyAttendance(
    id: Long = 1L,
    attendanceDate: LocalDate = LocalDate.now(),
    deviceName: String = "테스트 단말기",
    attendanceCount: Int = 10,
    workContent: String? = null,
): Attendance =
    Attendance(
        attendanceDate = attendanceDate,
        deviceName = deviceName,
        attendanceCount = attendanceCount,
        workContent = workContent,
    ).withId(id)
