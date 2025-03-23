package com.eeo.tradebuddy.parser.kr

import com.eeo.tradebuddy.model.TradeItem
import com.eeo.tradebuddy.model.TradeBulkRequest

fun parseEugeneMessage(message: String): TradeBulkRequest {
    return if (message.contains("USD") || message.contains("해외주식")) {
        parseOverseasEugene(message)
    } else {
        parseDomesticEugene(message)
    }
}

// ✅ 공통 TradeItem 생성 함수
fun createTradeItem(
    stockSymbol: String,
    stockName: String,
    tradeTime: String,
    price: Double,
    quantity: Int,
    tradeType: String = "BUY",
    source: String = "eugene",
    marketCountry: String
): TradeItem {
    return TradeItem(
        user_id = 1,
        stock_symbol = stockSymbol,
        stock_name = stockName,
        trade_time = tradeTime,
        trade_price = price,
        trade_quantity = quantity,
        trade_type = tradeType,
        message_source = source,
        trade_status = "CONFIRMED",
        market_type = marketCountry
    )
}

// ✅ 해외 주식 메시지 파서
fun parseOverseasEugene(message: String): TradeBulkRequest {
    val priceRegex = Regex("가격\\s?:\\s?([\\d.]+)USD")
    val qtyRegex = Regex("수량\\s?:\\s?(\\d+)주")
    val stockRegex = Regex("종목\\s?:\\s?(.*?)\\s*\\[(.*?)\\]")

    val stockMatch = stockRegex.find(message)
    val stockName = stockMatch?.groupValues?.get(1)?.trim() ?: "UNKNOWN"
    val stockSymbol = stockMatch?.groupValues?.get(2)?.trim() ?: "UNKNOWN"
    val price = priceRegex.find(message)?.groupValues?.get(1)?.toDoubleOrNull() ?: 0.0
    val quantity = qtyRegex.find(message)?.groupValues?.get(1)?.toIntOrNull() ?: 0

    val item = createTradeItem(
        stockSymbol = stockSymbol,
        stockName = stockName,
        tradeTime = "2025-03-22 13:00:00", // 테스트용
        price = price,
        quantity = quantity,
        tradeType = "BUY",
        market_type = "US"
    )

    return TradeBulkRequest(trades = listOf(item))
}

// ✅ 국내 주식 메시지 파서
fun parseDomesticEugene(message: String): TradeBulkRequest {
    val regex = Regex("([가-힣A-Za-z0-9\\s]+)\\[(\\d+)](\\d{1,3}(,\\d{3})*)원\\s(\\d+)주")
    val match = regex.find(message)

    val stockName = match?.groupValues?.get(1)?.trim() ?: "UNKNOWN"
    val stockSymbol = match?.groupValues?.get(2)?.trim() ?: "UNKNOWN"
    val priceStr = match?.groupValues?.get(3)?.replace(",", "") ?: "0"
    val quantity = match?.groupValues?.get(5)?.toIntOrNull() ?: 0
    val price = priceStr.toDoubleOrNull() ?: 0.0

    val tradeType = if (message.contains("매도")) "SELL" else "BUY"

    val item = createTradeItem(
        stockSymbol = stockSymbol,
        stockName = stockName,
        tradeTime = "2025-03-22 13:00:00", // 테스트용
        price = price,
        quantity = quantity,
        tradeType = tradeType,
        market_type = "KR"
    )

    return TradeBulkRequest(trades = listOf(item))
}
