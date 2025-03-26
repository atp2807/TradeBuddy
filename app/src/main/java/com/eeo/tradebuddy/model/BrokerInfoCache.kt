package com.eeo.tradebuddy.model

data class BrokerInfo(
    val id: Int,
    val country: String,
    val alias: String,
    val sms_number: String,
    val kakao_channel_name: String
)

object BrokerInfoCache {
    private var brokerList: List<BrokerInfo>? = null

    fun getAll(): List<BrokerInfo> {
        return brokerList ?: emptyList()
    }

    fun set(data: List<BrokerInfo>) {
        brokerList = data
    }

    fun clear() {
        brokerList = null
    }

    fun isInitialized(): Boolean {
        return brokerList != null
    }
}
