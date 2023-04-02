package com.example.binancewebsocketapp.data

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class BinanceCoinItem(
    @Json(name = "s") var symbol: String = "",
    @Json(name = "P") var priceChangePercent: String = "",
    @Json(name = "c") var lastPrice: String = ""
)
