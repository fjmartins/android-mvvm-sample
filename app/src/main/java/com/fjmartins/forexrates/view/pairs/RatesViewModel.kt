package com.fjmartins.forexrates.view.pairs

import android.annotation.SuppressLint
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.fjmartins.forexrates.model.Currency
import com.fjmartins.forexrates.repository.ForexRepository
import com.fjmartins.forexrates.model.Pair
import javax.inject.Inject

class RatesViewModel @Inject constructor(private val repository: ForexRepository) : ViewModel() {

    val currencies = MutableLiveData<List<Currency>>()
    val pairs = MutableLiveData<List<Pair>>()
    val loading = MutableLiveData<Boolean>()

    @SuppressLint("CheckResult")
    fun getCurrencies() {
        loading.value = true

        repository.getCurrencies { currencies ->
            this.currencies.value = currencies
            loading.value = false
        }
    }

    @SuppressLint("CheckResult")
    private fun getLiveRates(source: String) {
        loading.value = true

        repository.getLivePairs(source) { pairs ->
            this.pairs.value = pairs
            loading.value = false
        }
    }

    fun setSelectedCurrency(id: Int) {
        val currency = currencies.value?.get(id)
        getLiveRates(currency?.name ?: "USD")
    }
}