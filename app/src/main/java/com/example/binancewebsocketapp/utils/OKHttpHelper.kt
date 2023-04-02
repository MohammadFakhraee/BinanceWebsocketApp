package com.example.binancewebsocketapp.utils

import com.example.binancewebsocketapp.data.BinanceWebsocketRequestResponse
import com.squareup.moshi.Moshi
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.WebSocket
import java.util.concurrent.TimeUnit

fun createWebSocketClient(): OkHttpClient =
    OkHttpClient.Builder()
        .connectTimeout(10, TimeUnit.SECONDS)
        .writeTimeout(5, TimeUnit.SECONDS)
        .readTimeout(5, TimeUnit.SECONDS)
        .build()

fun createRequest(url: String): Request = Request.Builder().url(url).build()

fun parseFirstTimeWebsocketResponse(text: String) =
    Moshi.Builder().build().adapter(BinanceWebsocketRequestResponse::class.java).fromJson(text)