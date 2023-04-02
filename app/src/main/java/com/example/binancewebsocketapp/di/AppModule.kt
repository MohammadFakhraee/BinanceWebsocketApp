package com.example.binancewebsocketapp.di

import com.example.binancewebsocketapp.data.BinanceCoinItem
import com.example.binancewebsocketapp.utils.AppJsonAdapter
import com.squareup.moshi.JsonAdapter
import com.squareup.moshi.Moshi
import com.squareup.moshi.adapter
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Named

@Module
@InstallIn(SingletonComponent::class)
class AppModule {

    @OptIn(ExperimentalStdlibApi::class)
    @Provides
    @BinanceCoinItemListAppJsonAdapter
    fun provideListOfBinanceCoinItemJsonAdapter(): AppJsonAdapter<List<BinanceCoinItem>> =
        AppJsonAdapter(Moshi.Builder().build().adapter())
}
