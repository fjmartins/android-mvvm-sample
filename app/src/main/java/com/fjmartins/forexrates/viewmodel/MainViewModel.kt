package com.fjmartins.forexrates.viewmodel

import android.annotation.SuppressLint
import androidx.lifecycle.ViewModel
import com.fjmartins.forexrates.repository.ForexRepository
import com.fjmartins.forexrates.model.Pair
import javax.inject.Inject

class MainViewModel @Inject constructor(private val repository: ForexRepository) : ViewModel() {

    @SuppressLint("CheckResult")
    fun getLiveRates() {
        repository.getLiveRates().subscribe({ pairs ->
            displayQuotes(pairs)
        }, { e ->
            println(e)
        })
    }

    fun displayQuotes(pairs: List<Pair>) {

    }

}