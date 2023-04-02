package com.example.binancewebsocketapp.data

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class BinanceWebsocketRequestResponse(
    val result: String?,
    val id: Int?
)