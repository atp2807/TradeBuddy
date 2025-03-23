package com.eeo.tradebuddy.parser

val eugeneRegexMap = mapOf(
    "stock_name" to Regex("종목\\s?:\\s?(.*?)\\s*\\["),
    "stock_symbol" to Regex("\\[(.*?)\\]"),
    "trade_price" to Regex("가격\\s?:\\s?([\\d.]+)USD"),
    "trade_quantity" to Regex("수량\\s?:\\s?(\\d+)주"),
    "trade_type" to Regex("구분\\s?:\\s?(매수|매도)")
)

val samsungRegexMap = mapOf(
    "stock_name" to Regex("종목명[:：]\\s*(\\S+)"),
    "stock_symbol" to Regex("종목코드[:：]\\s*(\\S+)"),
    "trade_price" to Regex("체결단가[:：]\\s*([\\d,.]+)원"),
    "trade_quantity" to Regex("체결수량[:：]\\s*(\\d+)주"),
    "trade_type" to Regex("주문종류[:：]\\s*(매수|매도)")
)

val kiwoomRegexMap = mapOf(
    "stock_name" to Regex("종목명[:：]\\s*(\\S+)"),
    "stock_symbol" to Regex("종목코드[:：]\\s*(\\S+)"),
    "trade_price" to Regex("단가[:：]\\s*([\\d,.]+)원"),
    "trade_quantity" to Regex("수량[:：]\\s*(\\d+)주"),
    "trade_type" to Regex("구분[:：]\\s*(매수|매도)")
)
