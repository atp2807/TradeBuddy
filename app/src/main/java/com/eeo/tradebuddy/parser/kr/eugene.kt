package com.eeo.tradebuddy.parser.kr

import com.eeo.tradebuddy.model.TradeItem
import com.eeo.tradebuddy.model.TradeBulkRequest

fun parseEugeneMessage(message: String): TradeBulkRequest {
    val priceRegex = Regex("가격\\s?:\\s?([\\d.]+)USD")
    val qtyRegex = Regex("수량\\s?:\\s?(\\d+)주")

    // 1️⃣ 종목 정보 파싱
    val stockSectionStart = message.indexOf("종목")
    val stockSection = if (stockSectionStart != -1) {
        message.substring(stockSectionStart)
    } else {
        ""
    }

    // 2️⃣ 티커 추출: 종목 부분 내에서 첫 [] 안의 텍스트
    val tickerRegex = Regex("\\[(.*?)]")
    val stockSymbol = tickerRegex.find(stockSection)?.groupValues?.get(1)?.trim() ?: "UNKNOWN"

    // 3️⃣ 종목명 추출: ':' 다음부터 '[' 이전까지
    val stockName = if (stockSection.contains(":") && stockSection.contains("[")) {
        stockSection.substringAfter(":").substringBefore("[").trim()
    } else {
        "UNKNOWN"
    }

    // 4️⃣ 가격, 수량
    val price = priceRegex.find(message)?.groupValues?.get(1)?.toDoubleOrNull() ?: 0.0
    val quantity = qtyRegex.find(message)?.groupValues?.get(1)?.toIntOrNull() ?: 0

    // 5️⃣ TradeItem 생성
    val item = TradeItem(
        user_id = 1,
        stock_symbol = stockSymbol,
        stock_name = stockName,
        trade_time = "2025-03-22 13:00:00",
        trade_price = price,
        trade_quantity = quantity,
        trade_type = "BUY",
        message_source = "eugene",
        trade_status = "CONFIRMED"
    )

    return TradeBulkRequest(trades = listOf(item))
}