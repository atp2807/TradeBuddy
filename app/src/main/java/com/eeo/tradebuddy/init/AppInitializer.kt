package com.eeo.tradebuddy.init

import android.util.Log
import com.eeo.tradebuddy.model.BrokerInfoCache
import com.eeo.tradebuddy.model.FieldNameCache
import com.eeo.tradebuddy.network.RetrofitInstance
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

object AppInitializer {

    suspend fun init() = withContext(Dispatchers.IO) {
        loadFieldNamesIfNeeded()
        loadBrokerInfoIfNeeded()
    }

    private suspend fun loadFieldNamesIfNeeded() {
        if (FieldNameCache.fieldNames == null) {
            try {
                val response = RetrofitInstance.api.getFieldNames()
                val fieldNames = response.body() ?: emptyMap()
                FieldNameCache.fieldNames = fieldNames
                Log.d("AppInit", "✅ 필드네임 로드 성공 (${fieldNames.size}개)")
            } catch (e: Exception) {
                FieldNameCache.fieldNames = emptyMap()
                Log.e("AppInit", "❌ 필드네임 로드 실패", e)
            }
        }
    }

    private suspend fun loadBrokerInfoIfNeeded() {
        if (!BrokerInfoCache.isInitialized()) {
            try {
                val response = RetrofitInstance.api.getBrokers()
                val brokers = response.body() ?: emptyList()
                BrokerInfoCache.set(brokers)
                Log.d("AppInit", "✅ 브로커 인포 로드 성공 (${brokers.size}개)")
            } catch (e: Exception) {
                BrokerInfoCache.set(emptyList())
                Log.e("AppInit", "❌ 브로커 인포 로드 실패", e)
            }
        }
    }
}
