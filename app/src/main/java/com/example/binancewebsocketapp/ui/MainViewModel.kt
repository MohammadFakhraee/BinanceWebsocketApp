package com.example.binancewebsocketapp.ui

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.binancewebsocketapp.data.BinanceCoinItem
import com.example.binancewebsocketapp.di.BinanceCoinItemListAppJsonAdapter
import com.example.binancewebsocketapp.socket.BinanceWebsocketManager
import com.example.binancewebsocketapp.socket.WebSocketManager
import com.example.binancewebsocketapp.socket.WebsocketCallback
import com.example.binancewebsocketapp.utils.AppJsonAdapter
import com.example.binancewebsocketapp.utils.BINANCE_SUBSCRIBE_TICKER
import com.example.binancewebsocketapp.utils.BINANCE_WS_URL
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    @BinanceCoinItemListAppJsonAdapter appJsonAdapter: AppJsonAdapter<List<BinanceCoinItem>>
) : ViewModel(), WebsocketCallback<List<BinanceCoinItem>> {
    private var webSocketManager: WebSocketManager<List<BinanceCoinItem>> =
        BinanceWebsocketManager(BINANCE_WS_URL, BINANCE_SUBSCRIBE_TICKER, appJsonAdapter, this)

    private val _isConnecting = MutableStateFlow(false)
    val isConnecting: StateFlow<Boolean> = _isConnecting

    private val _coinList: MutableStateFlow<List<BinanceCoinItem>> = MutableStateFlow(arrayListOf())
    val coinList: StateFlow<List<BinanceCoinItem>> = _coinList

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error

    init {
        viewModelScope.launch(Dispatchers.IO) {
            webSocketManager.connect()
            _isConnecting.value = true
        }
    }

    override fun onConnectSuccess() {
        Log.i(TAG, "onConnectSuccess")
        _isConnecting.value = false
        _error.value = null
    }

    override fun onConnectFailed(t: Throwable) {
        Log.i(TAG, "onConnectFailed: ${t.message}")
        _isConnecting.value = false
        _error.value = t.message ?: "Couldn't connect to websocket. Unknown error."
    }

    override fun onClose() {
        Log.i(TAG, "onClose")
    }

    override fun onResponse(response: List<BinanceCoinItem>?) {
        Log.i(TAG, "onResponse: $response")
        _coinList.value = response ?: arrayListOf()
    }

    companion object {
        private const val TAG = "MainViewModel"
    }
}