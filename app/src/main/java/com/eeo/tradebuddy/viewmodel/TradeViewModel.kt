package com.eeo.tradebuddy.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.eeo.tradebuddy.model.TradeBulkRequest
import com.eeo.tradebuddy.network.RetrofitInstance
import kotlinx.coroutines.launch
import android.util.Log

class TradeViewModel : ViewModel() {

    fun uploadTrades(tradeRequest: TradeBulkRequest) {
        viewModelScope.launch {
            try {
                val response = RetrofitInstance.api.uploadTrades(tradeRequest)
                if (response.isSuccessful) {
                    Log.d("TradeViewModel", "업로드 성공: ${response.body()?.message}")
                } else {
                    Log.e("TradeViewModel", "업로드 실패: ${response.code()}")
                }
            } catch (e: Exception) {
                Log.e("TradeViewModel", "에러: ${e.message}")
            }
        }
    }
}
