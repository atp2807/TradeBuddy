package com.eeo.tradebuddy.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import com.eeo.tradebuddy.ui.screen.HomeScreen
import com.eeo.tradebuddy.ui.screen.AiAnalyzeScreen
import com.eeo.tradebuddy.ui.screen.ReportScreen
import com.eeo.tradebuddy.ui.screen.PremiumScreen
import com.eeo.tradebuddy.ui.screen.SplashScreen
import com.eeo.tradebuddy.ui.screen.KakaoGuideScreen
import com.google.accompanist.navigation.animation.AnimatedNavHost
import androidx.compose.animation.*
import androidx.compose.material3.Text

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun AppNavGraph(navController: NavHostController) {
    AnimatedNavHost(
        navController = navController,
        startDestination = NavRoutes.SPLASH,
        enterTransition = { slideInHorizontally { 1000 } },
        exitTransition = { slideOutHorizontally { -1000 } },
        popEnterTransition = { slideInHorizontally { -1000 } },
        popExitTransition = { slideOutHorizontally { 1000 } }
    ) {
        // 애니메이션은 여기서 공통 적용되고
        // 아래 composable엔 transition 없어도 됨!
        composable(NavRoutes.SPLASH) { SplashScreen(navController) }
        composable(NavRoutes.HOME) { HomeScreen(navController) }
        composable(NavRoutes.ANALYZE) { AiAnalyzeScreen(navController) }
        composable(NavRoutes.REPORT) { ReportScreen(navController) }
        composable(NavRoutes.PREMIUM) { PremiumScreen(navController) }
        composable(NavRoutes.KAKAO_GUIDE) { KakaoGuideScreen(navController) }
        composable(NavRoutes.WAIT_FOR_SAHTE) { Text("공유된 텍스트 처리 화면") }
    }
}
