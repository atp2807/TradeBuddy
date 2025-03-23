package com.eeo.tradebuddy.model

fun TradeItem.toDynamicJson(): Map<String, Any> {
    val fields = FieldNameCache.fieldNames ?: error("Field definitions are not loaded")
    return mapOf(
        fields["user_id"]!! to this.user_id,
        fields["stock_symbol"]!! to this.stock_symbol,
        fields["stock_name"]!! to this.stock_name,
        fields["trade_time"]!! to this.trade_time,
        fields["trade_price"]!! to this.trade_price,
        fields["trade_quantity"]!! to this.trade_quantity,
        fields["trade_type"]!! to this.trade_type,
        fields["message_source"]!! to this.message_source,
        fields["trade_status"]!! to this.trade_status,
        fields["market_type"]!! to this.market_type
    )
}

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
