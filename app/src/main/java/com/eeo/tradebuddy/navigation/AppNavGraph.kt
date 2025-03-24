package com.eeo.tradebuddy.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.eeo.tradebuddy.ui.screen.HomeScreen
import com.eeo.tradebuddy.ui.screen.AiAnalyzeScreen
import com.eeo.tradebuddy.ui.screen.ReportScreen
import com.eeo.tradebuddy.ui.screen.PremiumScreen
import com.eeo.tradebuddy.ui.screen.SplashScreen

@Composable
fun AppNavGraph(navController: NavHostController) {
    NavHost(
        navController = navController,
        startDestination = NavRoutes.SPLASH
    ) {
        composable(NavRoutes.SPLASH) {
            SplashScreen(navController)
        }

        composable(NavRoutes.HOME) {
            HomeScreen(navController)
        }
        composable(NavRoutes.ANALYZE) {
            AiAnalyzeScreen(navController)
        }
        composable(NavRoutes.REPORT) {
            ReportScreen(navController)
        }
        composable(NavRoutes.PREMIUM) {
            PremiumScreen(navController)
        }
    }
}
