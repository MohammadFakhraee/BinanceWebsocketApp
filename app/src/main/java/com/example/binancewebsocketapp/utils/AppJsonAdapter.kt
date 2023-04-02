package com.example.binancewebsocketapp.utils

import com.squareup.moshi.JsonAdapter
import com.example.binancewebsocketapp.socket.BinanceWebsocketManager

/**
 * Making [BinanceWebsocketManager] independent from json parser.
 */
class AppJsonAdapter<T>(private val jsonAdapter: JsonAdapter<T>) {
    fun fromJson(text: String): T? = jsonAdapter.fromJson(text)
    fun toJson(item: T): String? = jsonAdapter.toJson(item)
}