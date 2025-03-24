package com.eeo.tradebuddy.ui.screen

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.eeo.tradebuddy.navigation.NavRoutes
import com.eeo.tradebuddy.ui.components.AdUnitIds
import com.eeo.tradebuddy.ui.components.ButtonCard
import com.eeo.tradebuddy.ui.theme.AppSizes
import com.eeo.tradebuddy.R
import com.eeo.tradebuddy.ui.components.NativeAdPopupUI
import com.eeo.tradebuddy.ui.components.rememberLoadedNativeAd
import androidx.compose.runtime.getValue
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue

@Composable
fun HomeScreen(navController: NavController) {
    val isPremiumUser = false
    var showAdPopup by rememberSaveable { mutableStateOf(!isPremiumUser) } // 무료 사용자일 경우 true
    val nativeAdState = rememberLoadedNativeAd() // 내부에서 테스트 ID 사용 중

    Box(modifier = Modifier.fillMaxSize()) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(AppSizes.PaddingMedium),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(
                text = "TradeBuddy",
                style = MaterialTheme.typography.headlineLarge,
                color = Color(0xFF374151),
                modifier = Modifier.padding(bottom = 24.dp, top = 16.dp)
            )

            ButtonCard(
                text = stringResource(id = R.string.btn_analyze),
                backgroundColor = Color(0xFF6366F1)
            ) {
                navController.navigate(NavRoutes.ANALYZE)
            }

            Spacer(modifier = Modifier.height(12.dp))

            ButtonCard(
                text = stringResource(id = R.string.btn_view_report),
                backgroundColor = Color(0xFF0EA5E9)
            ) {
                navController.navigate(NavRoutes.REPORT)
            }

            Spacer(modifier = Modifier.height(12.dp))

            ButtonCard(
                text = stringResource(id = R.string.btn_premium_menu),
                backgroundColor = Color(0xFF4338CA)
            ) {
                navController.navigate(NavRoutes.PREMIUM)
            }
        }
    }
        if (showAdPopup) {
            NativeAdPopupUI(
                nativeAd = nativeAdState.value,
                onClose = { showAdPopup = false }
            )
        }
    }
}
