package com.fjmartins.forexrates.network

import com.fjmartins.forexrates.model.LiveQuotesResponse
import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.Query

interface CurrencyLayerApi {
    companion object {
        const val URL = "http://api.currencylayer.com/"
    }

    @GET("live")
    fun getLiveQuotes(@Query("access_key") key: String = "3b2146170c4eb839eab7a15ee06b74e5"): Single<LiveQuotesResponse>
}