package com.example.binancewebsocketapp.socket

interface WebSocketManager<T> {
    fun connect()
    fun reconnect(t: Throwable? = null)
    fun sendMessage(item: T)
    fun close()
}