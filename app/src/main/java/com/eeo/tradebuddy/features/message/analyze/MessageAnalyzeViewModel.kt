package com.eeo.tradebuddy.features.message.analyze

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.eeo.tradebuddy.features.message.filter.CombinedMessageFilter
import com.eeo.tradebuddy.features.message.read.SmsMessage
import com.eeo.tradebuddy.features.message.read.SmsMessageReader
import com.eeo.tradebuddy.features.message.model.BrokerMessageSummary
import com.eeo.tradebuddy.model.BrokerInfo
import com.eeo.tradebuddy.model.BrokerInfoCache
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*
import com.eeo.tradebuddy.model.TradeBulkRequest
import com.eeo.tradebuddy.network.RetrofitInstance
import android.util.Log
import com.eeo.tradebuddy.features.message.parser.kr.eugene.parseEugene

class MessageAnalyzeViewModel(application: Application) : AndroidViewModel(application) {

    var currentAnalyzeTime: Long = System.currentTimeMillis()
    private val _summaryList = MutableStateFlow<List<BrokerMessageSummary>>(emptyList())
    val summaryList: StateFlow<List<BrokerMessageSummary>> = _summaryList
    private var filteredMessages: Map<Int, List<SmsMessage>> = emptyMap()

    object UserInfoCache {
        var lastUploadedAt: Long = 0L // 서버에서 가져와 초기화
    }

    private fun toSummary(broker: BrokerInfo, messages: List<SmsMessage>): BrokerMessageSummary {
        val sdf = SimpleDateFormat("yy.MM.dd", Locale.KOREA)
        val sorted = messages.sortedBy { it.timestamp }
        val start = sdf.format(Date(sorted.first().timestamp))
        val end = sdf.format(Date(sorted.last().timestamp))
        return BrokerMessageSummary(
            brokerId = broker.id,
            alias = broker.alias,
            displayName = broker.kakao_channel_name,
            count = messages.size,
            dateRange = "$start ~ $end"
        )
    }

    fun toggleChecked(brokerId: Int) {
        _summaryList.value = _summaryList.value.map {
            if (it.brokerId == brokerId) it.copy(isChecked = !it.isChecked) else it
        }
    }

    fun loadFilteredMessages() {
        viewModelScope.launch {
            val context = getApplication<Application>()
            val messages = SmsMessageReader.readSmsMessages(
                context = context,
                startTimeMillis = UserInfoCache.lastUploadedAt,
                endTimeMillis = currentAnalyzeTime // <- 요기 반영
            )
            val brokers = BrokerInfoCache.getAll()
            val country = "KR"
            val sdf = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.KOREA)
            Log.d("⏱️TimeDebug", "📥 lastUploadedAt = ${UserInfoCache.lastUploadedAt} (${sdf.format(Date(UserInfoCache.lastUploadedAt))})")
            Log.d("⏱️TimeDebug", "📤 currentAnalyzeTime = $currentAnalyzeTime (${sdf.format(Date(currentAnalyzeTime))})")
            val filtered = CombinedMessageFilter.filter(messages, brokers, country)

            val grouped = filtered.mapNotNull { sms ->
                val broker = brokers.find { broker ->
                    sms.address.contains(broker.sms_number) ||
                            sms.address.contains(broker.kakao_channel_name) ||
                            sms.body.contains(broker.kakao_channel_name)
                }
                broker?.id?.let { it to (sms to broker) }
            }.groupBy(
                keySelector = { it.first },       // brokerId
                valueTransform = { it.second.first }
            )

            filteredMessages = grouped

            val summaries = grouped.mapNotNull { (brokerId, msgs) ->
                val broker = brokers.find { it.id == brokerId }
                broker?.let { toSummary(it, msgs) }
            }

            _summaryList.value = summaries
        }
    }
    fun uploadParsedTrades() {
        viewModelScope.launch {
            val brokers = BrokerInfoCache.getAll()
            val parsedItems = filteredMessages.flatMap { (brokerId, messages) ->
                val broker = brokers.find { it.id == brokerId }  ?: return@flatMap emptyList()

                messages.mapNotNull { msg ->
                    try {
                        val item = parseEugene(msg.body, msg.timestamp)?.trades?.firstOrNull()
                        if (item != null) {
                            Log.d("TradeUpload", "✅ 파싱 성공: ${msg.body}")
                        } else {
                            Log.w("TradeUpload", "❌ 파싱 실패(비어 있음): ${msg.body}")
                        }
                        item
                        // TODO: 나중에 broker에 따라 분기 필요
                        //val result = parseEugene(msg.body, msg.timestamp)  // <- 현재 Eugene만
                        //Log.d("TradeUpload", "✅ 파싱 성공: ${msg.body}")
                        //result.trades.firstOrNull() // TradeItem 하나만 사용
                    } catch (e: Exception) {
                        Log.w("TradeUpload", "❌ 파싱 실패: ${msg.body}")
                        null // 파싱 실패한 건 무시
                    }
                }
            }

            if (parsedItems.isEmpty()) {
                Log.w("TradeUpload", "업로드할 데이터가 없습니다")
                return@launch
            }

            val request = TradeBulkRequest(trades = parsedItems)
            val response = RetrofitInstance.api.uploadTrades(request)

            if (response.isSuccessful) {
                Log.d("TradeUpload", "✅ 업로드 성공")
                // 🟢 서버에 currentAnalyzeTime 기록
                // UserInfoCache.lastUploadedAt = currentAnalyzeTime // optional
            } else {
                Log.e("TradeUpload", "❌ 업로드 실패: ${response.code()}")
            }
        }
    }
    fun addKakaoSharedText(text: String) {
        val parsedLines = text.lines().filter { it.isNotBlank() }

        val messages = parsedLines.mapNotNull { line ->
            try {
                val result = parseEugene(line, System.currentTimeMillis())  // timestamp는 대충 현재 시간
                if (result?.trades?.isNotEmpty() == true) {
                    result.trades.first()  // 첫 번째 거래만 사용
                } else null
            } catch (e: Exception) {
                null  // 파싱 실패는 무시
            }
        }

        if (messages.isEmpty()) {
            Log.w("KakaoImport", "공유된 텍스트에서 파싱된 거래 없음")
            Log.d("KakaoImport", "원본 메시지:\n$text")
            return
        }

        val summary = BrokerMessageSummary(
            brokerId = -999,
            alias = "kakao_share",
            displayName = "유진증권(카톡)",
            count = messages.size,
            dateRange = "방금 공유됨",
            isChecked = true
        )

        _summaryList.value = _summaryList.value + summary
    }


}