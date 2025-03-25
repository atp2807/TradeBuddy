package com.eeo.tradebuddy.features.message.filter

import android.util.Log
import com.eeo.tradebuddy.model.BrokerInfo
import com.eeo.tradebuddy.features.message.read.SmsMessage

// 국가별 거래 키워드 관리
object MarketKeywords {
    val KR = listOf("매수", "매도")
    val US = listOf("BUY", "SELL")
    val JP = listOf("購入", "売却") // 예시

    fun forCountry(code: String): List<String> = when (code.uppercase()) {
        "KR" -> KR
        "US" -> US
        "JP" -> JP
        else -> emptyList()
    }
}

// 증권사 발신자 필터
object SmsBrokerFilter {
    fun isFromBroker(message: SmsMessage, brokers: List<BrokerInfo>): Boolean {
        val messageNumber = normalize(message.address)
        return brokers.any { broker ->
            normalize(broker.sms_number) == messageNumber
        }
    }

    private fun normalize(phone: String): String {
        return phone.filter { it.isDigit() }.takeLast(8)
    }
}

//거래 키워드 필터
object TradeKeywordFilter {
    fun containsTradeKeyword(message: SmsMessage, countryCode: String): Boolean {
        val body = message.body.replace("\\s".toRegex(), "") // 공백 제거
        return MarketKeywords.forCountry(countryCode).any { keyword ->
            body.contains(keyword)
        }
    }
}

//최종 필터 조합
object CombinedMessageFilter {
    fun filter(messages: List<SmsMessage>, brokers: List<BrokerInfo>, countryCode: String): List<SmsMessage> {
        val filtered = messages.filter { message ->
            SmsBrokerFilter.isFromBroker(message, brokers) &&
                    TradeKeywordFilter.containsTradeKeyword(message, countryCode)
        }
        Log.d("CombinedFilter", "📩 전체 메시지 ${messages.size}개 중 ${filtered.size}개 필터링됨")
        filtered.forEach {
            Log.d("FilteredMessage", "✅ ${it.address} | ${it.body}")
        }
        return filtered
    }
}
