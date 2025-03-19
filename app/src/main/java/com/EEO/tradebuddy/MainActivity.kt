package com.EEO.tradebuddy

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.unit.dp


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            setContent{
                MainScreen()
            }
        }
    }
}

@Composable
fun MainScreen() {
    Column(
        modifier = Modifier.fillMaxSize().padding(20.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("TradeBuddy", style = MaterialTheme.typography.headlineMedium)
        Spacer(modifier = Modifier.height(20.dp))

        Button(onClick = { /* 분석 시작 */ }) {
            Text("📊 분석 시작")
        }
        Spacer(modifier = Modifier.height(10.dp))

        Button(onClick = { /* 내 분석 기록 보기 */ }) {
            Text("🔍 내 분석 기록 보기")
        }
        Spacer(modifier = Modifier.height(10.dp))

        Button(onClick = { /* 관심 주식 등록 */ }) {
            Text("⭐ 관심 주식 등록")
        }
    }
}