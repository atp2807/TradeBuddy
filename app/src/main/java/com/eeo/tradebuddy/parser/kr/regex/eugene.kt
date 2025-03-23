package com.eeo.tradebuddy.parser.kr.regex

val eugeneOverseasRegexMap = mapOf(
    "stock_name" to Regex("종목\\s?:\\s?(.*?)\\s*\\["),
    "stock_symbol" to Regex("\\[(.*?)\\]"),
    "trade_price" to Regex("가격\\s?:\\s?([\\d.]+)USD"),
    "trade_quantity" to Regex("수량\\s?:\\s?(\\d+)주"),
    "trade_type" to Regex("구분\\s?:\\s?(매수|매도|매수체결|매도체결)")
)
