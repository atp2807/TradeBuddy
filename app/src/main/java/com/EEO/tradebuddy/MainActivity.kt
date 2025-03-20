package com.EEO.tradebuddy

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.material3.AlertDialog
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.runtime.remember
import com.EEO.tradebuddy.ui.theme.AppSizes

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {  // ✅ setContent 중복 제거
            MainScreen()
        }
    }
}

@Composable
fun MainScreen() {
    var showDialog = remember{ mutableStateOf(false)}
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background) // ✅ 테마 색상 적용
            .padding(AppSizes.PaddingMedium), // ✅ 상수 적용
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        // 앱 타이틀
        Text(
            text = "TradeBuddy",
            style = MaterialTheme.typography.headlineLarge.copy(fontWeight = FontWeight.Bold),
            color = Color(0xFF374151),
            modifier = Modifier.padding(bottom = 16.dp)
        )

        // 버튼 리스트
        ButtonCard("📊 분석 시작", Color(0xFF6366F1)) {
            showDialog.value = true  // ✅ 버튼 클릭 시 Dialog 표시
            println("showDialog 값 변경됨: ${showDialog.value}")
        }
        Spacer(modifier = Modifier.height(12.dp))
        ButtonCard("🔍 내 분석 기록 보기", Color(0xFF4F46E5)) { /* 분석 기록 화면 이동 */ }
        Spacer(modifier = Modifier.height(12.dp))
        ButtonCard("⭐ 관심 주식 등록", Color(0xFF4338CA)) { /* 관심 주식 관리 화면 이동 */ }
    }
    // 거래 데이터 입력 다이얼로그
    if (showDialog.value) {
        TradeDataInputDialog(
            onDismiss = { showDialog.value = false },
            onConfirm = { stock, time, price ->
                // TODO: 데이터 저장 로직 추가
                showDialog.value = false
            }
        )
    }
}

// 카드형 버튼 스타일
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
    var stockName = remember { mutableStateOf("") }
    var tradeTime = remember { mutableStateOf("") }
    var tradePrice = remember { mutableStateOf("") }

    AlertDialog(
        onDismissRequest = { onDismiss() },
        title = { Text("거래 데이터 입력") },
        text = {
            Column {
                OutlinedTextField(
                    value = stockName.value,
                    onValueChange = { stockName.value = it },
                    label = { Text("종목명") }
                )
                Spacer(modifier = Modifier.height(8.dp))
                OutlinedTextField(
                    value = tradeTime.value,
                    onValueChange = { tradeTime.value = it },
                    label = { Text("매매 시간 (예: 10:30 AM)") }
                )
                Spacer(modifier = Modifier.height(8.dp))
                OutlinedTextField(
                    value = tradePrice.value,
                    onValueChange = { tradePrice.value = it },
                    label = { Text("매매 가격") },
                    keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number)
                )
            }
        },
        confirmButton = {
            Button(onClick = { onConfirm(stockName.value, tradeTime.value, tradePrice.value) }) {
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
