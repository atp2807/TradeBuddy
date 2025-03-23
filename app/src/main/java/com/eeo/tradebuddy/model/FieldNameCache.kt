package com.eeo.tradebuddy.model


object FieldNameCache {
    var fieldNames: Map<String, String>? = null

    fun get(key: String): String {
        return fieldNames?.get(key) ?: key // 없으면 기본값
    }
}