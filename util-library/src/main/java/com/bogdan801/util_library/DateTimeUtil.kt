package com.bogdan801.util_library

import kotlinx.datetime.*
import kotlinx.datetime.TimeZone.Companion.currentSystemDefault

fun getCurrentDateTime(): LocalDateTime = Clock.System.now().toLocalDateTime(currentSystemDefault())

fun LocalDate.toFormattedString() =
    "${dayOfMonth.toString().padStart(2, '0')}.${monthNumber.toString().padStart(2, '0')}.$year"

fun LocalDateTime.toFormattedTime() =
    "${hour.toString().padStart(2, '0')}:${minute.toString().padStart(2, '0')}"
