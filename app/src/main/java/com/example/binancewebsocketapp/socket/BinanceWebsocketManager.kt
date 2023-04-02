package com.example.binancewebsocketapp.socket

import android.util.Log
import com.example.binancewebsocketapp.utils.AppJsonAdapter
import com.example.binancewebsocketapp.utils.createRequest
import com.example.binancewebsocketapp.utils.createWebSocketClient
import com.example.binancewebsocketapp.utils.parseFirstTimeWebsocketResponse
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import okhttp3.WebSocket
import okhttp3.WebSocketListener
import okio.ByteString

class BinanceWebsocketManager<T>(
    url: String,
    private val requestStr: String,
    private val responseJsonAdapter: AppJsonAdapter<T>,
    private val websocketCallback: WebsocketCallback<T>
) : WebSocketManager<T>, WebSocketListener() {
    private val client: OkHttpClient = createWebSocketClient()
    private val request: Request = createRequest(url)

    private var websocket: WebSocket? = null
    private var isConnect = false
    private var connectionTries = 0
    private var firstResponse = false

    override fun connect() {
        if (isConnect) return
        this.websocket = client.newWebSocket(request, this)
    }

    override fun reconnect(t: Throwable?) {
        if (connectionTries < MAX_TRIES) {
            try {
                Thread.sleep(MILLIS)
                connect()
                connectionTries++
            } catch (e: InterruptedException) {
                e.printStackTrace ()
            }
        } else {
            val error = "reconnect: number of tries exceeded. max tries: " +
                    "$MAX_TRIES, connectionTries: $connectionTries"
            Log.i(TAG, error)
            websocketCallback.onConnectFailed(t ?: Throwable(error))
        }
    }

    override fun sendMessage(item: T) {
        responseJsonAdapter.toJson(item).takeIf { message -> message != null }
            ?.let { message -> websocket?.send(message) } ?: Log.i(
            TAG, "sendMessage: couldn't parse item to string: $item"
        )
    }

    override fun close() {
        if (isConnect) {
            websocket?.let {
                it.cancel()
                it.close(CLOSE_CODE, "Connection closed due to client request")
            }
        }
    }

    override fun onOpen(webSocket: WebSocket, response: Response) {
        this.websocket = webSocket
        isConnect = response.code() == 101
        if (!isConnect) reconnect()
        else {
            websocketCallback.onConnectSuccess()
            Log.i(TAG, "onOpen: Successfully connected")

            this.websocket?.send(requestStr)
            firstResponse = false
        }
    }

    override fun onMessage(webSocket: WebSocket, bytes: ByteString) {
        Log.i(TAG, "onMessage1")
        onMessageCallback(webSocket, bytes.base64())
    }

    override fun onMessage(webSocket: WebSocket, text: String) {
        Log.i(TAG, "onMessage2: data = $text")
        onMessageCallback(webSocket, text)
    }

    override fun onClosing(webSocket: WebSocket, code: Int, reason: String) {
        Log.i(TAG, "onClosing")
        isConnect = false
        websocketCallback.onClose()
    }

    override fun onClosed(webSocket: WebSocket, code: Int, reason: String) {
        Log.i(TAG, "onClosed")
        isConnect = false
        websocketCallback.onClose()
    }

    override fun onFailure(webSocket: WebSocket, t: Throwable, response: Response?) {
        t.printStackTrace()
        Log.i(TAG, "onFailure: Error occurred: throwable = $t, response = $response")
        isConnect = false
        reconnect(t)
    }

    private fun onMessageCallback(webSocket: WebSocket, text: String) {
        if (!firstResponse) {
            firstResponse = true
            val firstResponse = parseFirstTimeWebsocketResponse(text)
            firstResponse?.takeIf { it.result != null }?.let {
                webSocket.close(CLOSE_CODE, "request was not successful: ${it.result}")
            }
        } else {
            val response = responseJsonAdapter.fromJson(text)
            websocketCallback.onResponse(response)
        }
    }

    companion object {
        private val TAG = BinanceWebsocketManager::class.java.simpleName
        private const val MAX_TRIES: Int = 5  // number of connection tries
        private const val MILLIS: Long = 1000  // reconnection interval in millis
        private const val CLOSE_CODE = 5001
    }
}