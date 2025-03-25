package com.eeo.tradebuddy.features.message.parser.kr.eugen

import com.eeo.tradebuddy.model.TradeBulkRequest
import com.eeo.tradebuddy.features.message.parser.autoParse
import com.eeo.tradebuddy.utils.TradeItemFactory
import com.eeo.tradebuddy.utils.getNowTimeString

fun parseEugeneMessage(message: String): TradeBulkRequest {
    val cleanedMessage = message.replace("[Web발신]", "").trim()
    val marketType = if (message.contains("USD") || message.contains("해외주식")) "US" else "KR"

    val meta = mapOf(
        "user_id" to 1,
        "message_source" to "eugene",
        "market_type" to marketType,
        "trade_time" to getNowTimeString(),
        "trade_status" to "CONFIRMED"
    )

    val result = autoParse(message, eugeneOverseasRegexMap, meta)
    val item = TradeItemFactory.fromMap(result)
    return TradeBulkRequest(trades = listOf(item))
}
