package com.example.githubsearchusers.core

import android.content.Context
import com.chuckerteam.chucker.api.ChuckerCollector
import com.chuckerteam.chucker.api.ChuckerInterceptor
import com.chuckerteam.chucker.api.RetentionManager
import com.example.githubsearchusers.BuildConfig
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import java.util.concurrent.TimeUnit

private const val BASE_URL = "https://api.github.com/"

private fun httpClient(context: Context): OkHttpClient {
    val clientBuilder = OkHttpClient.Builder()
        .readTimeout(10, TimeUnit.SECONDS)
        .connectTimeout(10, TimeUnit.SECONDS)

    if (BuildConfig.DEBUG) {
        val collector = ChuckerCollector(
            context = context,
            showNotification = true,
            retentionPeriod = RetentionManager.Period.ONE_HOUR
        )
        val chuckerInterceptor = ChuckerInterceptor(
            context = context,
            collector = collector,
            maxContentLength = 250000L,
            headersToRedact = emptySet<String>()
        )
        clientBuilder.addInterceptor(chuckerInterceptor)
    }

    return clientBuilder.build()
}

fun createMoshi(): Moshi = Moshi.Builder()
    .add(KotlinJsonAdapterFactory())
    .build()

fun createRetrofitClient(context: Context): Retrofit = Retrofit.Builder()
    .baseUrl(BASE_URL)
    .client(httpClient(context))
    .addConverterFactory(MoshiConverterFactory.create(createMoshi()))
    .build()