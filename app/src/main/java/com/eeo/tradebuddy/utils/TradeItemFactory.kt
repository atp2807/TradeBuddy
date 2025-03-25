package com.eeo.tradebuddy.utils

import com.eeo.tradebuddy.model.TradeItem
import kotlin.reflect.full.primaryConstructor

object TradeItemFactory {
    fun fromMap(map: Map<String, Any?>): TradeItem {
        val constructor = TradeItem::class.primaryConstructor!!
        val args = constructor.parameters.associateWith { param ->
            val value = map[param.name]
            when (param.type.classifier) {
                Int::class -> (value as? String)?.toSafeInt() ?: value as? Int
                Double::class -> (value as? String)?.toSafeDouble() ?: value as? Double
                Boolean::class -> (value as? String)?.toBooleanStrictOrNull() ?: value as? Boolean
                else -> value
            }
        }
        return constructor.callBy(args)
    }
}

fun String.toSafeInt(): Int? = this.replace(",", "").toIntOrNull()
fun String.toSafeDouble(): Double? = this.replace(",", "").toDoubleOrNull()
