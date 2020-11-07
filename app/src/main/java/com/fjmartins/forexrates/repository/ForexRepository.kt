package com.fjmartins.forexrates.repository

import android.annotation.SuppressLint
import androidx.room.EmptyResultSetException
import com.fjmartins.forexrates.dao.PairsDao
import com.fjmartins.forexrates.model.Pair
import com.fjmartins.forexrates.model.Quotes
import com.fjmartins.forexrates.network.CurrencyLayerApi
import com.fjmartins.forexrates.util.ForexUtils
import io.reactivex.Flowable
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import java.util.concurrent.TimeUnit
import kotlin.reflect.full.memberProperties

@SuppressLint("CheckResult")
class ForexRepository(
    private val pairsDao: PairsDao,
    private val currencyLayerApi: CurrencyLayerApi
) {

    fun getPairs(finally: (pairs: List<Pair>) -> Unit) {
        getPairsLocal().subscribe({ localPairs ->
            val expirationTime = System.currentTimeMillis() - localPairs[0].timestamp
            if (expirationTime >= TimeUnit.MINUTES.toMillis(30)) {
                getPairsRemote().subscribe { remotePairs ->
                    finally(remotePairs)
                }

                return@subscribe
            }

            finally(localPairs)
        }, { error ->
            if (error is EmptyResultSetException)
                getPairsRemote().subscribe { remotePairs ->
                    finally(remotePairs)
                }
        })
    }

    private fun getPairsLocal(): Single<List<Pair>> {
        return pairsDao.getAllPairs()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .map { pairs ->
                if (pairs.isEmpty()) throw EmptyResultSetException("Pairs table is empty")
                pairs
            }
    }

    private fun getPairsRemote(): Single<List<Pair>> {
        return currencyLayerApi.getLiveQuotes()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .repeatWhen { Flowable.timer(20, TimeUnit.SECONDS).repeat() }
            .firstOrError()
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
                savePairs(pairs)

                pairs.toList()
            }
    }

    private fun savePairs(pairs: List<Pair>) {
        pairsDao.insertPairs(pairs)
            .subscribeOn(Schedulers.io())
            .observeOn(Schedulers.computation())
            .subscribe()
    }
}