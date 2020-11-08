package com.fjmartins.forexrates.network

import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.Query

interface CurrencyLayerApi {
    companion object {
        const val KEY = "3b2146170c4eb839eab7a15ee06b74e5"
        const val URL = "http://api.currencylayer.com/"
    }

    @GET("live")
    fun getLiveQuotes(@Query("access_key") key: String = KEY, @Query("source") source: String = "USD"): Single<LiveQuotesResponse>

    @GET("list")
    fun getListOfCurrencies(@Query("access_key") key: String = KEY): Single<CurrencyListResponse>
}