package com.eeo.tradebuddy

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.ui.text.input.KeyboardType
import androidx.lifecycle.lifecycleScope
import com.eeo.tradebuddy.model.TradeBulkRequest
import com.eeo.tradebuddy.network.RetrofitInstance
import com.eeo.tradebuddy.ui.theme.AppSizes
import kotlinx.coroutines.launch
import com.eeo.tradebuddy.parser.kr.parseEugeneMessage
import com.eeo.tradebuddy.model.FieldNameCache
import com.eeo.tradebuddy.model.toDynamicJson
import com.eeo.tradebuddy.model.TradeItem

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        lifecycleScope.launch {
            fetchFieldNamesFromServer()
        }
        setContent {
            MainScreen()
        }
    }
}

@Composable
fun MainScreen() {
    var showDialog by remember { mutableStateOf(false) }
    val coroutineScope = rememberCoroutineScope()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(AppSizes.PaddingMedium),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = "TradeBuddy",
            style = MaterialTheme.typography.headlineLarge.copy(fontWeight = FontWeight.Bold),
            color = Color(0xFF374151),
            modifier = Modifier.padding(bottom = 16.dp)
        )

        ButtonCard("📊 분석 시작", Color(0xFF6366F1)) {
            val message = "해외주식 체결 안내 ㆍ계좌 : ***320 ㆍ종목 : T-REX 2X I [MSTZ] ㆍ구분 : 매수체결 [#2794] ㆍ가격 : 15.31USD ㆍ수량 : 288주"
            val parsedRequest = parseEugeneMessage(message)
            val dynamicRequest = mapOf("trades" to parsedRequest.trades.map { it.toDynamicJson() })

            coroutineScope.launch {
                try {
                    val response = RetrofitInstance.api.uploadTradesDynamic(dynamicRequest)
                    if (response.isSuccessful) {
                        println("✅ 업로드 성공: ${response.body()?.message}")
                    } else {
                        println("❌ 업로드 실패: ${response.errorBody()?.string()}")
                    }
                } catch (e: Exception) {
                    println("🚨 네트워크 오류: ${e.localizedMessage}")
                }
            }
        }

        Spacer(modifier = Modifier.height(12.dp))
        ButtonCard("🔍 내 분석 기록 보기", Color(0xFF4F46E5)) { /* TODO */ }
        Spacer(modifier = Modifier.height(12.dp))
        ButtonCard("⭐ 관심 주식 등록", Color(0xFF4338CA)) { /* TODO */ }
    }

    if (showDialog) {
        TradeDataInputDialog(
            onDismiss = { showDialog = false },
            onConfirm = { stock, time, price ->
                val tradeItem: TradeItem = TradeItem(
                    user_id = 1,
                    stock_symbol = stock,
                    stock_name = "임시 종목명",
                    trade_time = "2025-03-22T$time:00",
                    trade_price = price.toDoubleOrNull() ?: 0.0,
                    trade_quantity = 10,
                    trade_type = "BUY",
                    message_source = "app",
                    trade_status = "CONFIRMED",
                    market_type = "KR"
                )

                val request = TradeBulkRequest(trades = listOf(tradeItem))

                coroutineScope.launch {
                    try {
                        val response = RetrofitInstance.api.uploadTrades(request)
                        if (response.isSuccessful) {
                            println("✅ 업로드 성공: ${response.body()?.message}")
                        } else {
                            println("❌ 실패: ${response.errorBody()?.string()}")
                        }
                    } catch (e: Exception) {
                        println("🚨 에러 발생: ${e.localizedMessage}")
                    }
                }

                showDialog = false
            }
        )
    }
}

@Composable
fun ButtonCard(text: String, backgroundColor: Color, onClick: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(56.dp)
            .clickable { onClick() }
            .padding(horizontal = 8.dp),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = backgroundColor)
    ) {
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier.fillMaxSize()
        ) {
            Text(
                text = text,
                style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.Bold),
                color = Color.White
            )
        }
    }
}

@Composable
fun TradeDataInputDialog(
    onDismiss: () -> Unit,
    onConfirm: (String, String, String) -> Unit
) {
    var stockName by remember { mutableStateOf("") }
    var tradeTime by remember { mutableStateOf("") }
    var tradePrice by remember { mutableStateOf("") }

    AlertDialog(
        onDismissRequest = { onDismiss() },
        title = { Text("거래 데이터 입력") },
        text = {
            Column {
                OutlinedTextField(
                    value = stockName,
                    onValueChange = { stockName = it },
                    label = { Text("종목명") }
                )
                Spacer(modifier = Modifier.height(8.dp))
                OutlinedTextField(
                    value = tradeTime,
                    onValueChange = { tradeTime = it },
                    label = { Text("매매 시간 (예: 10:30)") }
                )
                Spacer(modifier = Modifier.height(8.dp))
                OutlinedTextField(
                    value = tradePrice,
                    onValueChange = { tradePrice = it },
                    label = { Text("매매 가격") },
                    keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number)
                )
            }
        },
        confirmButton = {
            Button(onClick = {
                onConfirm(stockName, tradeTime, tradePrice)
            }) {
                Text("확인")
            }
        },
        dismissButton = {
            Button(onClick = { onDismiss() }) {
                Text("취소")
            }
        }
    )
}

suspend fun fetchFieldNamesFromServer() {
    try {
        val response = RetrofitInstance.api.getFieldNames()
        if (response.isSuccessful) {
            FieldNameCache.fieldNames = response.body()
            println("✅ FieldNames 불러오기 성공: ${FieldNameCache.fieldNames}")
        } else {
            println("❌ FieldNames 응답 실패: ${response.errorBody()?.string()}")
        }
    } catch (e: Exception) {
        println("🚨 네트워크 오류: ${e.localizedMessage}")
    }
}
