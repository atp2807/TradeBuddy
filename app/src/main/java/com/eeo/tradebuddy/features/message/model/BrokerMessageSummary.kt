package com.eeo.tradebuddy.features.message.model

/**
 * 화면에 표시할 체크박스 한 줄 정보
 * 증권사별 메시지 수, 날짜 범위 등 UI용
 * ViewModel → Composable로 전달되는 데이터
 * 필터링용
 **/

data class BrokerMessageSummary(
    val brokerId: Int,
    val alias: String,
    val displayName: String,
    val count: Int,
    val dateRange: String,
    var isChecked: Boolean = true
)