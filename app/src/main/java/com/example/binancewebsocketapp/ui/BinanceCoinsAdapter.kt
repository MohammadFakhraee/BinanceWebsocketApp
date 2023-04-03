package com.example.binancewebsocketapp.ui

import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.example.binancewebsocketapp.R
import com.example.binancewebsocketapp.data.BinanceCoinItem
import com.example.binancewebsocketapp.databinding.ItemCoinBinding

class BinanceCoinsAdapter :
    ListAdapter<BinanceCoinItem, BinanceCoinsAdapter.BinanceCoinsViewHolder>(BinanceCoinDiffUtil()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BinanceCoinsViewHolder =
        BinanceCoinsViewHolder(
            ItemCoinBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        )

    override fun onBindViewHolder(holder: BinanceCoinsViewHolder, position: Int) {
        holder.onBind(getItem(position))
    }

    inner class BinanceCoinsViewHolder(private val binding: ItemCoinBinding) :
        ViewHolder(binding.root) {

        fun onBind(item: BinanceCoinItem) {
            binding.run {
                symbolTv.text = item.symbol
                currentValueTv.text = item.lastPrice
                changeInPercentTv.text =
                    root.context.getString(R.string.percent, item.priceChangePercent)
                val color = if (item.priceChangePercent.first() == '-') Color.RED
                else Color.GREEN
                changeInPercentTv.setTextColor(color)
            }
        }
    }
}

class BinanceCoinDiffUtil() : DiffUtil.ItemCallback<BinanceCoinItem>() {
    override fun areItemsTheSame(oldItem: BinanceCoinItem, newItem: BinanceCoinItem): Boolean =
        oldItem.symbol == newItem.symbol

    override fun areContentsTheSame(oldItem: BinanceCoinItem, newItem: BinanceCoinItem): Boolean =
        oldItem == newItem
}