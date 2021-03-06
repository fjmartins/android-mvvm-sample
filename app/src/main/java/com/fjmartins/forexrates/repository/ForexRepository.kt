package com.fjmartins.forexrates.repository

import androidx.room.EmptyResultSetException
import com.fjmartins.forexrates.dao.ForexDatabase
import com.fjmartins.forexrates.model.Currency
import com.fjmartins.forexrates.model.Pair
import com.fjmartins.forexrates.network.CurrencyLayerApi
import com.fjmartins.forexrates.util.DisposableManager
import io.reactivex.Observable
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import java.util.concurrent.TimeUnit

//TODO: Refactor
class ForexRepository(
    private val forexDatabase: ForexDatabase,
    private val currencyLayerApi: CurrencyLayerApi
) {

    companion object {
        const val MINUTES_TO_REFRESH = 30L
    }

    fun getCurrencies(update: (currencies: List<Currency>) -> Unit) {
        DisposableManager.add(Observable.interval(0, MINUTES_TO_REFRESH, TimeUnit.MINUTES)
            .flatMap {
                return@flatMap Observable.create<List<Currency>> { emitter ->
                    getCurrenciesLocal().subscribe({ localCurrencies ->
                        val expirationTime =
                            System.currentTimeMillis() - localCurrencies[0].timestamp
                        if (expirationTime >= TimeUnit.MINUTES.toMillis(MINUTES_TO_REFRESH)) {
                            DisposableManager.add(getCurrenciesRemote().subscribe { remoteCurrencies ->
                                emitter.onNext(remoteCurrencies)
                                emitter.onComplete()
                            })

                            return@subscribe
                        }

                        emitter.onNext(localCurrencies)
                        emitter.onComplete()
                    }, { error ->
                        if (error is EmptyResultSetException)
                            DisposableManager.add(getCurrenciesRemote().subscribe { remoteCurrencies ->
                                emitter.onNext(remoteCurrencies)
                                emitter.onComplete()
                            })
                    })
                }
            }
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe { currencies ->
                update(currencies)
            })
    }

    private fun getCurrenciesLocal(): Single<List<Currency>> {
        return forexDatabase.currencyDao().getAllCurrencies()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .map { currencies ->
                if (currencies.isEmpty()) throw EmptyResultSetException("Currencies table is empty")

                currencies
            }
    }

    private fun getCurrenciesRemote(): Single<List<Currency>> {
        return currencyLayerApi.getListOfCurrencies()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .map { currencyListResponse ->
                val currencies = ArrayList<Currency>()
                for ((key, value) in currencyListResponse.currencies) {
                    currencies.add(
                        Currency(
                            name = key,
                            description = value,
                            timestamp = System.currentTimeMillis()
                        )
                    )
                }
                saveCurrencies(currencies)

                currencies.toList()
            }
    }

    private fun saveCurrencies(currencies: List<Currency>) {
        forexDatabase.currencyDao().insertCurrencies(currencies)
            .subscribeOn(Schedulers.io())
            .observeOn(Schedulers.computation())
            .subscribe()
    }

    fun getLivePairs(update: (pairs: List<Pair>) -> Unit) {
        DisposableManager.add(Observable.interval(0, MINUTES_TO_REFRESH, TimeUnit.MINUTES)
            .flatMap {
                return@flatMap Observable.create<List<Pair>> { emitter ->
                    getPairsLocal().subscribe({ localPairs ->
                        val expirationTime = System.currentTimeMillis() - localPairs[0].timestamp
                        if (expirationTime >= TimeUnit.MINUTES.toMillis(MINUTES_TO_REFRESH)) {
                            DisposableManager.add(getPairsRemote().subscribe { remotePairs ->
                                emitter.onNext(remotePairs)
                                emitter.onComplete()
                            })

                            return@subscribe
                        }

                        emitter.onNext(localPairs)
                        emitter.onComplete()
                    }, { error ->
                        if (error is EmptyResultSetException)
                            DisposableManager.add(getPairsRemote().subscribe { remotePairs ->
                                emitter.onNext(remotePairs)
                                emitter.onComplete()
                            })
                    })
                }
            }
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe { pairs ->
                update(pairs)
            })
    }

    private fun getPairsLocal(): Single<List<Pair>> {
        return forexDatabase.pairsDao().getAllPairs()
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
            .map { liveQuotesResponse ->
                liveQuotesResponse.quotes

                val pairs = ArrayList<Pair>()
                for ((key, value) in liveQuotesResponse.quotes) {
                    pairs.add(
                        Pair(
                            name = key,
                            value = value,
                            timestamp = System.currentTimeMillis()
                        )
                    )
                }
                savePairs(pairs)

                pairs.toList()
            }
    }

    private fun savePairs(pairs: List<Pair>) {
        forexDatabase.pairsDao().insertPairs(pairs)
            .subscribeOn(Schedulers.io())
            .observeOn(Schedulers.computation())
            .subscribe()
    }
}