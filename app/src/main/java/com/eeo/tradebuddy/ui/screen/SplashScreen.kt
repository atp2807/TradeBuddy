package com.eeo.tradebuddy.ui.screen

import android.app.Activity
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.eeo.tradebuddy.R
import com.eeo.tradebuddy.navigation.NavRoutes
import androidx.compose.ui.graphics.Color
import kotlinx.coroutines.delay
import com.eeo.tradebuddy.ui.components.rememberLoadedNativeAd
import com.eeo.tradebuddy.ui.components.NativeAdSplashUI
@Composable
fun SplashScreen(navController: NavController) {
    val context = LocalContext.current
    val activity = context as Activity

    val nativeAd = rememberLoadedNativeAd()

    LaunchedEffect(true) {
        delay(2000L)
        navController.navigate(NavRoutes.HOME) {
            popUpTo(NavRoutes.SPLASH) { inclusive = true }
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(32.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Top
    ) {
        Image(
            painter = painterResource(id = R.drawable.splash_logo),
            contentDescription = "브랜드 이미지",
            modifier = Modifier.fillMaxWidth().weight(1f)
        )
        Spacer(modifier = Modifier.height(16.dp))

        // 📦 광고 위치
        NativeAdSplashUI(nativeAd.value)
    }
}