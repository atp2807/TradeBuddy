package com.eeo.tradebuddy.ui.components

import android.app.Activity
import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.google.android.gms.ads.*
import com.google.android.gms.ads.nativead.NativeAd
import com.google.android.gms.ads.nativead.NativeAdOptions
import com.google.android.gms.ads.interstitial.InterstitialAd
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback

object AdUnitIds {
    const val TEST_INTERSTITIAL = "ca-app-pub-3940256099942544/5354046379"
    const val TEST_NATIVE = "ca-app-pub-3940256099942544/2247696110"

    const val PROD_REWARDED_INTERSTITIAL = "ca-app-pub-5485266772967256/8273730352"
    const val PROD_SPLASH_NATIVE = "ca-app-pub-5485266772967256/6980829981"
}

object InterstitialAdHelper {
    private var interstitialAd: InterstitialAd? = null

    fun loadAd(activity: Activity, adUnitId: String = AdUnitIds.TEST_INTERSTITIAL) {
        InterstitialAd.load(
            activity,
            adUnitId,
            AdRequest.Builder().build(),
            object : InterstitialAdLoadCallback() {
                override fun onAdLoaded(ad: InterstitialAd) {
                    interstitialAd = ad
                    Log.d("AdLog", "✅ 전면 광고 로딩 완료")
                }

                override fun onAdFailedToLoad(error: LoadAdError) {
                    Log.w("AdLog", "❌ 전면 광고 로딩 실패: ${error.message}")
                    interstitialAd = null
                }
            }
        )
    }

    fun showAdIfNotPremium(
        activity: Activity,
        isPremiumUser: Boolean,
        onDismiss: () -> Unit
    ) {
        if (isPremiumUser) {
            onDismiss()
            return
        }

        interstitialAd?.fullScreenContentCallback = object : FullScreenContentCallback() {
            override fun onAdDismissedFullScreenContent() {
                onDismiss()
                interstitialAd = null
            }

            override fun onAdFailedToShowFullScreenContent(adError: AdError) {
                onDismiss()
                interstitialAd = null
            }
        }

        interstitialAd?.show(activity) ?: onDismiss()
    }
}

@Composable
fun rememberLoadedNativeAd(): State<NativeAd?> {
    val context = LocalContext.current
    val nativeAdState = remember { mutableStateOf<NativeAd?>(null) }

    DisposableEffect(Unit) {
        val adLoader = AdLoader.Builder(context, AdUnitIds.TEST_NATIVE)
            .forNativeAd { ad -> nativeAdState.value = ad }
            .withAdListener(object : AdListener() {
                override fun onAdFailedToLoad(error: LoadAdError) {
                    Log.e("AdLog", "❌ 광고 로딩 실패: ${error.message}")
                }
            })
            .withNativeAdOptions(NativeAdOptions.Builder().build())
            .build()

        adLoader.loadAd(AdRequest.Builder().build())

        onDispose {
            nativeAdState.value?.destroy()
        }
    }

    return nativeAdState
}

@Composable
fun NativeAdSplashUI(nativeAd: NativeAd?) {
    if (nativeAd == null) {
        Box(
            modifier = Modifier
                .fillMaxWidth(0.9f)
                .height(100.dp)
                .background(Color.LightGray),
            contentAlignment = Alignment.Center
        ) {
            Text("광고 로딩 중...", color = Color.DarkGray)
        }
        return
    }

    Column(
        modifier = Modifier
            .fillMaxWidth(0.9f)
            .wrapContentHeight()
            .background(Color(0xFFF1F5F9))
            .padding(16.dp)
    ) {
        Text(
            text = nativeAd.headline ?: "광고 제목 없음",
            style = MaterialTheme.typography.titleMedium,
            color = Color.Black
        )

        Spacer(modifier = Modifier.height(8.dp))

        nativeAd.body?.let {
            Text(
                text = it,
                style = MaterialTheme.typography.bodyMedium,
                color = Color.DarkGray
            )
        }

        Spacer(modifier = Modifier.height(8.dp))

        nativeAd.callToAction?.let {
            Box(
                modifier = Modifier
                    .background(Color(0xFF6366F1))
                    .padding(horizontal = 16.dp, vertical = 8.dp)
            ) {
                Text(text = it, color = Color.White)
            }
        }
    }
}


@Composable
fun NativeAdPopupUI(
    nativeAd: NativeAd?,
    onClose: () -> Unit
) {
    if (nativeAd == null) return

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black.copy(alpha = 0.4f)),
        contentAlignment = Alignment.Center
    ) {
        Card(
            modifier = Modifier
                .fillMaxWidth(0.85f)
                .wrapContentHeight(),
            shape = RoundedCornerShape(16.dp)
        ) {
            Column(
                modifier = Modifier.padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(nativeAd.headline ?: "광고 제목", style = MaterialTheme.typography.titleMedium)
                Spacer(modifier = Modifier.height(8.dp))
                Button(onClick = { /* 광고 클릭 */ }) {
                    Text(nativeAd.callToAction ?: "자세히 보기")
                }
                Spacer(modifier = Modifier.height(16.dp))
                TextButton(onClick = onClose) {
                    Text("닫기", color = Color.Gray)
                }
            }
        }
    }
}


@Composable
fun NativeAdBox(
    isPremiumUser: Boolean = false,
    adUnitId: String = AdUnitIds.TEST_NATIVE,
    modifier: Modifier = Modifier
        .fillMaxWidth(0.9f)
        .height(100.dp)
) {
    if (isPremiumUser) return

    val context = LocalContext.current
    var nativeAd by remember { mutableStateOf<NativeAd?>(null) }

    DisposableEffect(Unit) {
        val adLoader = AdLoader.Builder(context, adUnitId)
            .forNativeAd { ad -> nativeAd = ad }
            .withAdListener(object : AdListener() {
                override fun onAdFailedToLoad(error: LoadAdError) {
                    Log.e("AdLog", "❌ 네이티브 광고 실패: ${error.message}")
                }
            })
            .withNativeAdOptions(NativeAdOptions.Builder().build())
            .build()

        adLoader.loadAd(AdRequest.Builder().build())

        onDispose { nativeAd?.destroy() }
    }

    Box(
        modifier = modifier
            .background(Color(0xFFF1F5F9)),
        contentAlignment = Alignment.Center
    ) {
        nativeAd?.let { ad ->
            Column(modifier = Modifier.padding(12.dp)) {
                Text(
                    text = ad.headline ?: "광고 제목",
                    style = MaterialTheme.typography.titleMedium
                )
                Spacer(modifier = Modifier.height(4.dp))
                Button(
                    onClick = { /* 추후 클릭 동작 */ },
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF6366F1))
                ) {
                    Text(ad.callToAction ?: "자세히 보기")
                }
            }
        } ?: Text("📦 광고 로딩 중...", color = Color.Gray)
    }
}
