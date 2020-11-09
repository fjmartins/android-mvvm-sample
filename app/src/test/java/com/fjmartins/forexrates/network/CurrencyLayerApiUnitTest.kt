package com.fjmartins.forexrates.network

import com.fjmartins.forexrates.base.BaseUnitTest
import junit.framework.Assert.assertEquals
import okhttp3.OkHttpClient
import org.junit.Before
import org.junit.Test
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory

class CurrencyLayerApiUnitTest : BaseUnitTest() {

    lateinit var currencyLayerApi: CurrencyLayerApi

    @Before
    fun setUp() {
        currencyLayerApi = Retrofit.Builder()
            .baseUrl(CurrencyLayerApi.URL)
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .addConverterFactory(GsonConverterFactory.create())
            .client(OkHttpClient.Builder().build())
            .build()
            .create(CurrencyLayerApi::class.java)
    }

    @Test
    fun getListOfCurrenciesTest() {
        val response = currencyLayerApi.getListOfCurrencies().blockingGet()
        assertEquals(true, response.success)
    }

    @Test
    fun getListOfCurrenciesWrongAccessKeyTest() {
        val response = currencyLayerApi.getListOfCurrencies(key = "wrong_key").blockingGet()
        assertEquals(false, response.success)
    }

    @Test
    fun getLiveQuotesTest() {
        val response = currencyLayerApi.getLiveQuotes().blockingGet()
        assertEquals(true, response.success)
    }

    @Test
    fun getLiveQuotesWrongKeyTest() {
        val response = currencyLayerApi.getLiveQuotes(key = "wrong_key").blockingGet()
        assertEquals(false, response.success)
    }
}