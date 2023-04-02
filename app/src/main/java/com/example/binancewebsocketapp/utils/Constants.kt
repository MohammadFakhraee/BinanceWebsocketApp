package com.example.binancewebsocketapp.utils

const val BINANCE_WS_URL = "wss://fstream.binance.com/ws"

const val BINANCE_SUBSCRIBE_TICKER = "{\n\"method\": \"SUBSCRIBE\",\n\"params\": [\n\"!ticker@arr\"\n]\n}"