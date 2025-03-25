package com.eeo.tradebuddy.ui.screen

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.eeo.tradebuddy.features.message.analyze.MessageAnalyzeViewModel
import com.eeo.tradebuddy.features.message.model.BrokerMessageSummary
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.eeo.tradebuddy.R
import com.eeo.tradebuddy.ui.components.ButtonCard
import com.eeo.tradebuddy.utils.SharedTextHolder


@Composable
fun AiAnalyzeScreen(navController: NavController) {
    val viewModel: MessageAnalyzeViewModel = viewModel()
    val summaryList by viewModel.summaryList.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.currentAnalyzeTime = System.currentTimeMillis()
        viewModel.loadFilteredMessages()

        val shared = SharedTextHolder.sharedText
        if (!shared.isNullOrBlank()) {
            viewModel.addKakaoSharedText(shared)
            SharedTextHolder.sharedText = null  // 중복 방지
        }
    }

    Column(modifier = Modifier
        .fillMaxSize()
        .padding(start = 16.dp, end = 16.dp, top = 16.dp,bottom = 40.dp)
    ) {
        Spacer(modifier = Modifier.height(72.dp))
        Text("📊 분석할 증권사 선택", style = MaterialTheme.typography.titleMedium)
        Spacer(modifier = Modifier.height(12.dp))

        LazyColumn(modifier = Modifier.weight(1f).fillMaxSize()) {
            items(summaryList) { item ->
                BrokerItemRow(item = item, onCheckedChange = {
                    viewModel.toggleChecked(item.brokerId)
                })
            }

            item {
                Spacer(modifier = Modifier.height(8.dp))
                AddKakaoButton {
                    navController.navigate("kakao_guide")
                }
                Spacer(modifier = Modifier.height(72.dp)) // 버튼 가려짐 방지용
            }
        }

        //Spacer(modifier = Modifier.height(16.dp))

        ButtonCard(
            text = stringResource(id = R.string.btn_generate_report),
            backgroundColor = Color(0xFF5C6BC0), // 보라색 톤 (네이티브 M3와 잘 어울림)
            onClick = {
                viewModel.currentAnalyzeTime = System.currentTimeMillis()
                //viewModel.loadFilteredMessages()
                viewModel.uploadParsedTrades()
                //val selected = summaryList.filter { it.isChecked }
                //viewModel.uploadParsedTrades()
            },
        )
    }
}

@Composable
fun BrokerItemRow(
    item: BrokerMessageSummary,
    onCheckedChange: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 6.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Checkbox(
            checked = item.isChecked,
            onCheckedChange = { onCheckedChange() }
        )
        Spacer(modifier = Modifier.width(8.dp))
        Column {
            Text(text = "${item.displayName} (문자 ${item.count}건)")
            Text(
                text = item.dateRange,
                style = MaterialTheme.typography.bodySmall
            )
        }
    }
}
@Composable
fun AddKakaoButton(onClick: () -> Unit) {
    Button(
        onClick = onClick,
        colors = ButtonDefaults.buttonColors(
            containerColor = Color.White,
            contentColor = Color.Black
        ),
        shape = RoundedCornerShape(50),
        elevation = ButtonDefaults.buttonElevation(2.dp),
        modifier = Modifier
            .padding(vertical = 12.dp)
    ) {
        Icon(Icons.Default.Add, contentDescription = null)
        Spacer(modifier = Modifier.width(6.dp))
        Text(text = "카카오톡 데이터 추가")
    }
}