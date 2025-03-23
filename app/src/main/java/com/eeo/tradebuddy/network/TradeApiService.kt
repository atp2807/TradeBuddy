package com.eeo.tradebuddy.network

import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import com.eeo.tradebuddy.model.TradeBulkRequest
import com.eeo.tradebuddy.model.UploadResult


interface TradeApiService {
    @POST("trades/bulk")
    suspend fun uploadTrades(@Body trades: TradeBulkRequest): Response<UploadResult>

    @POST("/trades/bulk")
    suspend fun uploadTradesDynamic(
        @Body trades: Map<String, @JvmSuppressWildcards Any>
    ): Response<UploadResult>

    @GET("/fieldnames")
    suspend fun getFieldNames(): Response<Map<String, String>>

}