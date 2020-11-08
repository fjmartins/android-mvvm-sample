package com.fjmartins.forexrates.view.rates

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.fjmartins.forexrates.repository.ForexRepository
import com.fjmartins.forexrates.model.Pair
import javax.inject.Inject

class RatesViewModel @Inject constructor(private val repository: ForexRepository) : ViewModel() {

    val pairs = MutableLiveData<List<Pair>>()

    fun calculateRates(selectedCurrency: String, amount: Double) {
        repository.getLivePairs { pairs ->

            var amountInDollar = 0.0

            for(pair in pairs) {
                if (pair.name.contains(selectedCurrency)) {
                    amountInDollar = amount / pair.value
                    break
                }
            }

            this.pairs.value = pairs.map { pair ->
                if (selectedCurrency != "USD")
                    pair.value = pair.value * amountInDollar
                else
                    pair.value = pair.value * amount

                pair
            }
        }
    }
}