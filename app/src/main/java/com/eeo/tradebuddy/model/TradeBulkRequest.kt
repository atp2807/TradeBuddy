package com.eeo.tradebuddy.model
import kotlin.reflect.full.memberProperties
import kotlin.reflect.KProperty1

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
    val fields = FieldNameCache.fieldNames ?: error("❌ Field names are not loaded")
    return this::class.memberProperties
        .filterIsInstance<KProperty1<TradeItem, *>>()
        .filter { fields.containsKey(it.name) }  // ✅ 캐시에 있는 필드만 필터링
        .associate { prop ->
            val key = fields[prop.name]!!         // ✅ 캐시에서 DB용 이름
            val value = prop.get(this)            // ✅ 해당 필드의 실제 값
            key to value
        }
}
