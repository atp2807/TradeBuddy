package com.EEO.tradebuddy.model

data class TradeItem(
    val user_id: Int,
    val stock_symbol: String,
    val trade_time: String,
    val trade_price: Double,
    val trade_quantity: Int,
    val trade_type: String,
    val message_source: String,
    val trade_status: String
)

data class TradeBulkRequest(
    val trades: List<TradeItem>
)
