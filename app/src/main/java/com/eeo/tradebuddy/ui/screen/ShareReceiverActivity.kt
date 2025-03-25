package com.eeo.tradebuddy.ui.screen

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.eeo.tradebuddy.ui.theme.TradeBuddyTheme
import android.util.Log
class ShareReceiverActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        var sharedText: String? = null
        val intentAction = intent?.action
        val intentType = intent?.type

        if (intentAction == Intent.ACTION_SEND) {
            // 텍스트 공유인 경우
            if (intentType == "text/plain") {
                sharedText = intent.getStringExtra(Intent.EXTRA_TEXT)
                Log.d("SHARE_RECEIVED", "받은 텍스트: $sharedText")
            }

            // 파일 공유인 경우
            if (intent.hasExtra(Intent.EXTRA_STREAM)) {
                val uri = intent.getParcelableExtra<Uri>(Intent.EXTRA_STREAM)
                uri?.let {
                    Log.d("SHARE_RECEIVED", "받은 파일 URI: $it")
                    val content = contentResolver.openInputStream(it)?.bufferedReader()?.readText()
                    Log.d("SHARE_RECEIVED", "파일 내용:\n$content")
                    sharedText = content
                }
            }
        }

        setContent {
            TradeBuddyTheme {
                Surface(modifier = Modifier.fillMaxSize()) {
                    sharedText?.let {
                        SharedTextAnalyzerScreen(it)
                    } ?: Text("공유된 텍스트가 없습니다.")
                }
            }
        }
    }
}
@Composable
fun SharedTextAnalyzerScreen(text: String) {
    Column(modifier = Modifier
        .padding(16.dp)
        .fillMaxSize()) {
        Text("공유된 내용:", style = MaterialTheme.typography.titleMedium)
        Spacer(modifier = Modifier.height(8.dp))
        Text(text, style = MaterialTheme.typography.bodyMedium)

        Spacer(modifier = Modifier.height(24.dp))

        Button(onClick = {
            Log.d("SHARE_ANALYZE", "💬 분석 시작: ${text.take(100)}")
            // TODO: 분석 로직 연결 예정
        }) {
            Text("이 텍스트 분석하기")
        }
    }
}