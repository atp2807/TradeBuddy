package com.EEO.tradebuddy.ui.theme

import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

// ✅ 다크 모드 색상 테마
private val DarkColorScheme = darkColorScheme(
    primary = Color(0xFF6366F1),
    secondary = Color(0xFF4F46E5),
    tertiary = Color(0xFF4338CA),
    background = Color(0xFF1C1C1E),
    onPrimary = Color.White,
    onSecondary = Color.White,
    onBackground = Color.White,
    onSurface = Color.White
)

// ✅ 라이트 모드 색상 테마
private val LightColorScheme = lightColorScheme(
    primary = Color(0xFF6366F1),
    secondary = Color(0xFF4F46E5),
    tertiary = Color(0xFF4338CA),
    background = Color(0xFFEEF2F3),
    onPrimary = Color.White,
    onSecondary = Color.White,
    onBackground = Color.Black,
    onSurface = Color.Black
)

// ✅ 글자 크기 & 여백 상수
object AppSizes {
    val TitleSize = 20.sp
    val ButtonTextSize = 16.sp
    val PaddingSmall = 8.dp
    val PaddingMedium = 16.dp
    val PaddingLarge = 24.dp
}

// ✅ TradeBuddy 전체 테마 적용
@Composable
fun TradeBuddyTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colorScheme = if (darkTheme) DarkColorScheme else LightColorScheme

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,  // ✅ 기본 텍스트 스타일
        content = content
    )
}
