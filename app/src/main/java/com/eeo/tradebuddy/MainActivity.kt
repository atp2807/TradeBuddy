package com.eeo.tradebuddy

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import com.eeo.tradebuddy.ui.theme.TradeBuddyTheme
import androidx.navigation.compose.rememberNavController

import com.eeo.tradebuddy.navigation.AppNavGraph
import com.google.android.gms.ads.MobileAds


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        MobileAds.initialize(this)

        setContent {
            TradeBuddyTheme {
                val navController = rememberNavController()
                AppNavGraph(navController = navController)
            }
        }
    }
}