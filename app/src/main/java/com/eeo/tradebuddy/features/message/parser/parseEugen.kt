package com.eeo.tradebuddy.features.message.parser.kr.eugene

import android.util.Log
import com.eeo.tradebuddy.features.message.parser.autoParse
import com.eeo.tradebuddy.model.TradeBulkRequest
import com.eeo.tradebuddy.utils.TradeItemFactory
import com.eeo.tradebuddy.utils.getNowTimeString
import java.time.LocalDateTime
import java.time.ZoneId
import java.text.SimpleDateFormat
import java.util.*

// Eugene 파서: 2024.11.15 이전/이후 분기
fun parseEugene(message: String, receivedAt: Long): TradeBulkRequest? {
    val cleanedMessage = message.replace("[Web발신]", "").trim()
    val isDomestic = message.contains("원") || message.contains("국내") || (
            message.contains("매수") || message.contains("매도")
            ) && !message.contains("USD")
    val marketType = if (isDomestic) "KR" else "US"

    val cutoffTime = LocalDateTime.of(2024, 11, 15, 8, 59)
        .atZone(ZoneId.of("Asia/Seoul"))
        .toInstant()
        .toEpochMilli()

    val regexMap = buildEugeneRegexMap(
        priceUnit = if (isDomestic) "원" else "USD",
        isNewFormat = receivedAt >= cutoffTime,
        symbolUnit = if (isDomestic) "\\d{4,6}" else "[A-Z]{1,10}"
    )

    val meta = mapOf(
        "user_id" to 1,
        "message_source" to "eugene",
        "market_type" to marketType,
        "trade_time" to formatEpochMillisToString(receivedAt),
        "trade_status" to "CONFIRMED"
    )

    return try {
        val parsed = autoParse(cleanedMessage, regexMap, meta) // ✅ 공통 regexMap 사용
        Log.d("ParseEugene", "🔍 trade_type 파싱 결과: ${parsed["trade_type"]}")
        val item = TradeItemFactory.fromMap(parsed)
        TradeBulkRequest(trades = listOf(item))
    } catch (e: Exception) {
        Log.e("ParseEugene", "❌ 파싱 오류: ${e.message}")
        null
    }
}

//2024.11.15 이후 포맷
val eugeneNewRegexTemplate = mapOf(
    "stock_name" to "종목\\s?:\\s?(.*?)\\s*\\[",
    "stock_symbol" to "\\[({SYMBOL_UNIT})\\]",
    "trade_price" to "가격\\s?:\\s?([\\d.,]+){PRICE_UNIT}",
    "trade_quantity" to "수량\\s?:\\s?([\\d,]+)주",
    "trade_type" to "구분\\s?:\\s?(매수|매도|매수체결|매도체결)"
)

//2024.11.15 이전 포맷
val eugeneOldRegexTemplate = mapOf(
    "stock_name" to "체결(.*?)\\[", // ✅ '매수체결', '매도체결' 이후 ~ '[' 전까지
    "stock_symbol" to "\\[({SYMBOL_UNIT})\\]",
    "trade_price" to "([\\d.,]+){PRICE_UNIT}",
    "trade_quantity" to "{PRICE_UNIT}\\s?([\\d,]+)주",
    "trade_type" to "(매수|매도|매수체결|매도체결)"
)
fun buildEugeneRegexMap(priceUnit: String, isNewFormat: Boolean, symbolUnit: String): Map<String, Regex> {
    val template = if (isNewFormat) eugeneNewRegexTemplate else eugeneOldRegexTemplate
    return template.mapValues { (_, pattern) ->
        Regex(
            pattern
                .replace("{PRICE_UNIT}", priceUnit)
                .replace("{SYMBOL_UNIT}", symbolUnit)
        )
    }
}
fun formatEpochMillisToString(epochMillis: Long): String {
    val sdf = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.KOREA)
    return sdf.format(Date(epochMillis))
}