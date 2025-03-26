package com.eeo.tradebuddy

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.eeo.tradebuddy.ui.theme.TradeBuddyTheme
import androidx.navigation.compose.rememberNavController

import com.eeo.tradebuddy.ui.navigation.AppNavGraph
import com.google.android.gms.ads.MobileAds
import android.Manifest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import android.content.pm.PackageManager
import android.util.Log
import androidx.lifecycle.lifecycleScope
import com.eeo.tradebuddy.model.BrokerInfoCache
import com.eeo.tradebuddy.model.FieldNameCache
import com.eeo.tradebuddy.network.RetrofitInstance
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    private val permissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions()
    ) { permissions ->
        // 권한 결과 처리
        val granted = permissions.entries.all { it.value }
        if (granted) {
            // 모든 권한 허용됨
        } else {
            // 거절됨: 경고 메시지 or 기능 제한
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestSmsPermissions()
        MobileAds.initialize(this)

        lifecycleScope.launch {
            com.eeo.tradebuddy.init.AppInitializer.init()
        }

        setContent {
            TradeBuddyTheme {
                val navController = rememberNavController()
                AppNavGraph(navController = navController)
            }
        }
    }

    private fun requestSmsPermissions() {
        val permissionsToRequest = arrayOf(
            Manifest.permission.READ_SMS,
            Manifest.permission.READ_PHONE_STATE,
            Manifest.permission.RECEIVE_SMS
        ).filter {
            ContextCompat.checkSelfPermission(this, it) != PackageManager.PERMISSION_GRANTED
        }

        if (permissionsToRequest.isNotEmpty()) {
            permissionLauncher.launch(permissionsToRequest.toTypedArray())
        }
    }
}