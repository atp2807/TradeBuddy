package com.eeo.tradebuddy.model


object FieldNameCache {
    var fieldNames: Map<String, String>? = null
}

/*서버에서 받아올 거니까 하드코딩으로 우선 진행*/
object FieldNameCache1 {
    var fieldNames: Map<String, String>? = mapOf(
        "user_id" to "user_id",
        "stock_symbol" to "stock_symbol",
        "stock_name" to "stock_name",
        "trade_time" to "trade_time",
        "trade_price" to "trade_price",
        "trade_quantity" to "trade_quantity",
        "trade_type" to "trade_type",
        "message_source" to "message_source",
        "trade_status" to "trade_status",
        "market_type" to "market_type"
    )
}
