package com.EEO.tradebuddy.network

import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST
import com.EEO.tradebuddy.model.TradeBulkRequest
import com.EEO.tradebuddy.model.UploadResult

interface TradeApiService {
    @POST("trades/bulk")
    suspend fun uploadTrades(@Body trades: TradeBulkRequest): Response<UploadResult>
}