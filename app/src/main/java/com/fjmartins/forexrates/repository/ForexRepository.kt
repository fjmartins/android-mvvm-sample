package com.fjmartins.forexrates.repository

import android.annotation.SuppressLint
import com.fjmartins.forexrates.dao.PairsDao
import com.fjmartins.forexrates.model.Pair
import com.fjmartins.forexrates.model.Quotes
import com.fjmartins.forexrates.network.CurrencyLayerService
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlin.reflect.full.memberProperties

@SuppressLint("CheckResult")
class ForexRepository(private val quotesDao: PairsDao, private val currencyLayerService: CurrencyLayerService) {

    fun getLiveRates(): Single<List<Pair>> {
        return currencyLayerService.getLiveQuotes()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .map { v ->
                // Workaround for bad response structure from the API
                val pairs = ArrayList<Pair>()
                for (prop in Quotes::class.memberProperties) {
                    pairs.add(Pair(name = prop.name, value = prop.get(v.quotes) as Double))
                }

                quotesDao.insertPairs(pairs)
                    .subscribeOn(Schedulers.io())
                    .observeOn(Schedulers.computation())
                    .subscribe({s ->
                        println(s)
                    }, { e ->
                        println(e)
                    })

                pairs.toList()
            }
    }
}