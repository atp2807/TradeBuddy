package com.eeo.tradebuddy.ui.screen

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.eeo.tradebuddy.R

@Composable
fun KakaoGuideScreen(navController: NavController) {
    val steps = listOf(
        Pair("1. 카카오톡에서 증권사 채팅방에 들어가세요", R.drawable.select_user),
        Pair("2. 우측 상단 메뉴 버튼을 누르세요", R.drawable.select_menu),
        Pair("3. 우측 하단의 설정 버튼을 누르세요", R.drawable.select_setting),
        Pair("4. '대화 내용 내보내기'를 선택하세요", R.drawable.out_chat),
        Pair("5. '텍스트만 보내기'를 선택하세요", R.drawable.select_text),
        Pair("6. 공유 가능한 앱 목록에서 앱을 찾아주세요", R.drawable.fine_app),
        Pair("7. 앱을 선택해 공유해주세요", R.drawable.select_app)
    )

    val pagerState = rememberPagerState(pageCount = { steps.size })
    val coroutineScope = rememberCoroutineScope()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "카카오톡 거래 데이터 내보내기 안내",
            style = MaterialTheme.typography.titleLarge
        )

        Spacer(modifier = Modifier.height(24.dp))

        HorizontalPager(state = pagerState, modifier = Modifier.weight(1f)) { page ->
            val (description, imageRes) = steps[page]
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.fillMaxSize()
            ) {
                Text(
                    text = description,
                    style = MaterialTheme.typography.bodyLarge,
                    modifier = Modifier.padding(vertical = 8.dp)
                )
                Image(
                    painter = painterResource(id = imageRes),
                    contentDescription = description,
                    modifier = Modifier
                        .fillMaxWidth()
                        .heightIn(max = 500.dp)
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        Row(
            horizontalArrangement = Arrangement.Center,
            modifier = Modifier.fillMaxWidth()
        ) {
            repeat(steps.size) { index ->
                val color = if (pagerState.currentPage == index) Color.DarkGray else Color.LightGray
                Box(
                    modifier = Modifier
                        .padding(4.dp)
                        .size(8.dp)
                        .background(color = color, shape = MaterialTheme.shapes.small)
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = {
                navController.navigate("waitForShare")
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp)
        ) {
            Text("카카오톡 열기")
        }
    }
}
