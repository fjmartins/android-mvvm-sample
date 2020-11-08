package com.fjmartins.forexrates.view.rates

import android.annotation.SuppressLint
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.fjmartins.forexrates.model.Currency
import com.fjmartins.forexrates.repository.ForexRepository
import com.fjmartins.forexrates.model.Pair
import javax.inject.Inject

class RatesViewModel @Inject constructor(private val repository: ForexRepository) : ViewModel() {

    private lateinit var selectedCurrency: Currency

    val currencies = MutableLiveData<List<Currency>>()
    val pairs = MutableLiveData<List<Pair>>()

    @SuppressLint("CheckResult")
    fun getCurrencies() {
        repository.getCurrencies { currencies ->
            this.currencies.value = currencies
        }
    }

    @SuppressLint("CheckResult")
    private fun getLiveRates() {
        repository.getLivePairs { pairs ->
            this.pairs.value = pairs
        }
    }

    fun setSelectedCurrency(id: Int) {
        currencies.value?.run {
            selectedCurrency = get(id)
        }

        getLiveRates()
    }

    fun setAmount(amount: Double) {
        var amountInDollar = 0.0

        for(pair in pairs.value.orEmpty()) {
            if (pair.name.contains(selectedCurrency.name)) {
                amountInDollar = amount / pair.value
            }
        }

        pairs.value = pairs.value?.map { pair ->
            if (selectedCurrency.name != "USD")
                pair.value = pair.value * amountInDollar
            else
                pair.value = pair.value * amount

            pair
        }
    }
}