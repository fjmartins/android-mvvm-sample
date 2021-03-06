package com.fjmartins.forexrates.view.currencies

import android.annotation.SuppressLint
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.fjmartins.forexrates.model.Currency
import com.fjmartins.forexrates.repository.ForexRepository
import javax.inject.Inject

class CurrencyViewModel @Inject constructor(private val repository: ForexRepository) : ViewModel() {

    val amount = MutableLiveData<String>()
    val selectedId = MutableLiveData<Int>()

    lateinit var selected: Currency
    val currencies = MutableLiveData<List<Currency>>()

    @SuppressLint("CheckResult")
    fun getCurrencies() {
        repository.getCurrencies { currencies ->
            this.currencies.value = currencies
        }
    }

    fun setSelectedCurrency(id: Int) {
        currencies.value?.run {
            selected = get(id)
        }

        selectedId.value = id
    }
}