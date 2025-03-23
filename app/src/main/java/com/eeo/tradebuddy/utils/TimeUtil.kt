package com.eeo.tradebuddy.utils

import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

fun getNowTimeString(): String {
    val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss")
    return LocalDateTime.now().format(formatter)
}