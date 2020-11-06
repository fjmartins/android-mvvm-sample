package com.fjmartins.forexrates.repository

import android.annotation.SuppressLint
import com.fjmartins.forexrates.dao.PairsDao
import com.fjmartins.forexrates.model.Pair
import com.fjmartins.forexrates.model.Quotes
import com.fjmartins.forexrates.network.CurrencyLayerService
import com.fjmartins.forexrates.util.ForexUtils
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlin.reflect.full.memberProperties

@SuppressLint("CheckResult")
class ForexRepository(
    private val quotesDao: PairsDao,
    private val currencyLayerService: CurrencyLayerService
) {

    fun getPairs(finally: (pairs: List<Pair>) -> Unit) {
        getPairsLocal().subscribe { localPairs ->
            if (localPairs.isEmpty())
                getPairsRemote().subscribe { remotePairs ->
                    finally(remotePairs)
                }
            else finally(localPairs)
        }
    }

    private fun getPairsLocal(): Single<List<Pair>> {
        return quotesDao.getAllPairs()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
    }

    private fun getPairsRemote(): Single<List<Pair>> {
        return currencyLayerService.getLiveQuotes()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .map { liveQuotesResponse ->
                // Using reflection to convert each quote into a currency Pair object
                val pairs = ArrayList<Pair>()
                for (prop in Quotes::class.memberProperties) {
                    pairs.add(
                        Pair(
                            name = prop.name,
                            description = ForexUtils.currencyDescriptionMap[prop.name.substring(3)].orEmpty(),
                            value = prop.get(liveQuotesResponse.quotes) as Double,
                            timestamp = System.currentTimeMillis()
                        )
                    )
                }

                // Save locally
                quotesDao.insertPairs(pairs)
                    .subscribeOn(Schedulers.io())
                    .observeOn(Schedulers.computation())
                    .subscribe()

                pairs.toList()
            }
    }
}