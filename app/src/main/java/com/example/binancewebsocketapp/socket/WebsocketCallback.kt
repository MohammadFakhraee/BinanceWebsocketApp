package com.example.binancewebsocketapp.socket

interface WebsocketCallback<in T> {
    fun onConnectSuccess()
    fun onConnectFailed(t: Throwable)
    fun onClose()
    fun onResponse(response: T?)
}