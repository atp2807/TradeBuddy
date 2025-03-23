package com.eeo.tradebuddy.model
import kotlin.reflect.full.memberProperties

data class TradeItem(
    val user_id: Int,
    val stock_symbol: String,
    val stock_name: String,
    val trade_time: String,
    val trade_price: Double,
    val trade_quantity: Int,
    val trade_type: String,
    val message_source: String,
    val trade_status: String,
    val market_type: String
)

data class TradeBulkRequest(
    val trades: List<TradeItem>
)
fun TradeItem.toDynamicJson(): Map<String, Any?> {
    return this::class.memberProperties.associate { prop ->
        val key = FieldNameCache.get(prop.name)
        val value = prop.get(this)
        key to value
    }
}