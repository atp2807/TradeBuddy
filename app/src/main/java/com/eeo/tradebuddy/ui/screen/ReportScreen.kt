package com.eeo.tradebuddy.ui.screen
import androidx.compose.runtime.Composable
import androidx.navigation.NavController
import androidx.compose.material3.Text
import androidx.compose.foundation.layout.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun ReportScreen(navController: NavController) {
    Column(modifier = Modifier.padding(16.dp)) {
        Text("report AI 분석 화면입니다.")
        // 추후 체크박스 리스트 들어갈 예정
    }
}