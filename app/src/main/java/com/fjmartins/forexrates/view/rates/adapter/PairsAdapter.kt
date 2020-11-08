package com.fjmartins.forexrates.view.rates.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.fjmartins.forexrates.databinding.PairItemBinding
import com.fjmartins.forexrates.model.Pair

class PairsAdapter : RecyclerView.Adapter<PairsAdapter.PairsHolder>() {

    private var pairs: List<Pair>? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PairsHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding = PairItemBinding.inflate(layoutInflater, parent, false)

        return PairsHolder(binding)
    }

    override fun getItemCount(): Int = pairs?.size ?: 0

    override fun onBindViewHolder(holder: PairsHolder, position: Int) {
        pairs?.run {
            val data = this[position]
            holder.binding.pair = data
        }
    }

    fun setCurrencyPairs(pairs: List<Pair>) {
        this.pairs = pairs

        notifyDataSetChanged()
    }

    class PairsHolder(var binding: PairItemBinding): RecyclerView.ViewHolder(binding.root)
}
