package com.eeo.tradebuddy.parser

import com.eeo.tradebuddy.utils.TradeItemFactory

fun autoParse(
    message: String,
    regexMap: Map<String, Regex>,
    meta: Map<String, Any?> = emptyMap()
): Map<String, Any?> {
    val parsed = mutableMapOf<String, Any?>()

    for ((field, regex) in regexMap) {
        val value = regex.find(message)?.groupValues?.get(1)?.trim()
        parsed[field] = value
    }

    return parsed + meta
}
