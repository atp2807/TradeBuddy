package com.eeo.tradebuddy.network

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import com.eeo.tradebuddy.BuildConfig
import okhttp3.HttpUrl.Companion.toHttpUrl

object RetrofitInstance {
    private val BASE_URL = BuildConfig.BASE_URL

    val api: TradeApiService by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL.toHttpUrl())
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(TradeApiService::class.java)
    }
}