package com.fjmartins.forexrates.view.pairs

import android.annotation.SuppressLint
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.fjmartins.forexrates.repository.ForexRepository
import com.fjmartins.forexrates.model.Pair
import javax.inject.Inject

class PairsViewModel @Inject constructor(private val repository: ForexRepository) : ViewModel() {

    val pairs = MutableLiveData<List<Pair>>()
    val loading = MutableLiveData<Boolean>()

    @SuppressLint("CheckResult")
    fun getLiveRates() {
        loading.value = true

        repository.getLiveRates().subscribe({ pairs ->
            this.pairs.value = pairs

            loading.value = false
        }, { e ->
            println(e)
        })
    }
}